# import collections
# import math
# import os
# import shutil
# import pandas as pd
# import torch
# import torchvision
# from torch import nn
# from d2l import torch as d2l
# import matplotlib.pyplot as plt
# import numpy as np
# from PIL import Image
#
#
#
#
# # 数据整理
# data_dir = os.path.join('.', 'dataset')
# # os.path.join() 把文件目录都放到一起变成一个 url
#
#
# # 数据分为训练数据集、验证数据集、测试数据集，并分别复制到对应的文件夹之中
# # 如果数据量不大（对于我个人的主机，几个G的数据还是可以这样做），但是数据太大就不建议了，这样数据会被复制两次
# def reorg_dog_data(data_dir, valid_ratio):
#     labels = d2l.read_csv_labels(os.path.join(data_dir, 'cats.csv'))    # 把cats.csv中的数据提取成字典
#     d2l.reorg_train_valid(data_dir, labels, valid_ratio)
#     d2l.reorg_test(data_dir)
#
#
# batch_size = 64
# valid_ratio = 0.1
# reorg_dog_data(data_dir, valid_ratio)
#
#
#
#
#
# # 图像增广
# transform_train = torchvision.transforms.Compose([
# # torchvision.transforms.Compose([...])是PyTorch中用于组合多个图像变换的函数。
# # 它接受一个变换列表作为参数，这个列表中可以包含多种不同的变换操作，例如裁剪、翻转、颜色变换、归一化等。
#
#
#
#     # 随机裁剪图像，所得图像为原始面积的0.08到1之间，高宽比在3/4和4/3之间。
#     # 然后，缩放图像以创建224 x 224的新图像
#     torchvision.transforms.RandomResizedCrop(224, scale=(0.08, 1.0),
#                                              ratio=(3.0 / 4.0, 4.0 / 3.0)),
#     torchvision.transforms.RandomHorizontalFlip(),
#     # 随机更改亮度，对比度和饱和度
#     torchvision.transforms.ColorJitter(brightness=0.4, contrast=0.4,
#                                        saturation=0.4),
#     # 添加随机噪声
#     torchvision.transforms.ToTensor(),
#     # 标准化图像的每个通道
#     torchvision.transforms.Normalize([0.485, 0.456, 0.406],
#                                      [0.229, 0.224, 0.225])])
#
#
# transform_test = torchvision.transforms.Compose([
#     torchvision.transforms.Resize(256),
#     # 从图像中心裁切224x224大小的图片
#     torchvision.transforms.CenterCrop(224),
#     torchvision.transforms.ToTensor(),
#     torchvision.transforms.Normalize([0.485, 0.456, 0.406],
#                                      [0.229, 0.224, 0.225])])
#
#
#
# # 加载数据
# # 注意四个datasets：train_valid_ds 是train_ds和valid_ds的集合（通常我们是根据这个数据集来划分train和valid的）
# train_ds, train_valid_ds = [
#     torchvision.datasets.ImageFolder(
#         os.path.join(data_dir, 'train_valid_test', folder),
#         transform=transform_train) for folder in ['train', 'train_valid']]
#
#
# valid_ds, test_ds = [
#     torchvision.datasets.ImageFolder(
#         os.path.join(data_dir, 'train_valid_test', folder),
#         transform=transform_test) for folder in ['valid', 'test']]
#
#
#
#
#
# # 创建 Dataloader 对象
# train_iter, train_valid_iter = [
#     torch.utils.data.DataLoader(dataset, batch_size, shuffle=True, drop_last=True)
#     for dataset in (train_ds, train_valid_ds)]
#
# valid_iter = torch.utils.data.DataLoader(valid_ds, batch_size, shuffle=False,
#                                          drop_last=True)
#
# test_iter = torch.utils.data.DataLoader(test_ds, batch_size, shuffle=False,
#                                         drop_last=False)
#
#
#
#
# # 微调模型
# def get_net(devices):
#     finetune_net = nn.Sequential()
#     finetune_net.features = torchvision.models.resnet50(pretrained=True)
#     # 定义一个新的输出网络，共有120个输出类别
#     finetune_net.output_new = nn.Sequential(nn.Linear(1000, 512), nn.ReLU(),
#                                             nn.Linear(512, 120))
#     # 将模型参数分配给用于计算的CPU或GPU
#     finetune_net = finetune_net.to(devices[0])
#     # 冻结参数
#     for param in finetune_net.features.parameters():
#         param.requires_grad = False
#     return finetune_net
#
#
#
#
#
#
#
# """计算损失之前，首先获取预训练模型的输出层之前的输出，然后使用这个输出作为我们自定义的输出层的输入，进行损失计算"""
# loss = nn.CrossEntropyLoss(reduction='none')
#
#
# def evaluate_loss(data_iter, net, devices):
#     l_sum, n = 0.0, 0
#     for features, labels in data_iter:
#         features, labels = features.to(devices[0]), labels.to(devices[0])
#         outputs = net(features)
#         l = loss(outputs, labels)
#         l_sum += l.sum()
#         n += labels.numel()  # 确保这里n会增加
#         if n == 0:  # 如果n为0，返回一个不会引发错误的值，比如0或None
#             return 0
#     return l_sum / n if n != 0 else float('inf')  # 避免除以0，如果n为0，返回无穷大
#
#
# def train(net, train_iter, valid_iter, num_epochs, lr, wd, devices, lr_period, lr_decay):
#     # 只训练小型自定义输出网络
#     net = nn.DataParallel(net, device_ids=devices).to(devices[0])
#     # 注意这里进行训练的部分是都可以求解梯度的部分（即我们自定义的部分）
#     trainer = torch.optim.SGD(
#         (param for param in net.parameters() if param.requires_grad), lr=lr, momentum=0.9, weight_decay=wd)
#     scheduler = torch.optim.lr_scheduler.StepLR(trainer, lr_period, lr_decay)
#     num_batches, timer = len(train_iter), d2l.Timer()
#     legend = ['train loss']
#     if valid_iter is not None:
#         legend.append('valid loss')
#     animator = d2l.Animator(xlabel='epoch', xlim=[1, num_epochs],
#                             legend=legend)
#     for epoch in range(num_epochs):
#         metric = d2l.Accumulator(2)
#         for i, (features, labels) in enumerate(train_iter):
#             timer.start()
#             features, labels = features.to(devices[0]), labels.to(devices[0])
#             trainer.zero_grad()
#             output = net(features)
#             l = loss(output, labels).sum()
#             l.backward()
#             trainer.step()
#             metric.add(l, labels.shape[0])
#             timer.stop()
#             if (i + 1) % (num_batches // 5) == 0 or i == num_batches - 1:
#                 animator.add(epoch + (i + 1) / num_batches,
#                              (metric[0] / metric[1], None))
#         measures = f'train loss {metric[0] / metric[1]:.3f}'
#         if valid_iter is not None:
#             valid_loss = evaluate_loss(valid_iter, net, devices)
#             # 检查 valid_loss 是否是张量
#             if isinstance(valid_loss, torch.Tensor):
#                 valid_loss = valid_loss.detach().cpu().numpy()
#             else:
#                 # 如果valid_loss不是一个张量，则直接使用它
#                 valid_loss = np.array(valid_loss)
#
#                 # 使用转换后的valid_loss调用 animator.add
#             animator.add(epoch + 1, (None, valid_loss))
#
#         scheduler.step()
#     if valid_iter is not None:
#         measures += f', valid loss {valid_loss:.3f}'
#
#     print(measures + f'\n{metric[1] * num_epochs / timer.sum():.1f}'
#                      f' examples/sec on {str(devices)}')
#
#
#     torch.save(net, 'complete_model.pth')    # 保存模型参数
#
#
#
#
#
# # 采用 Adam 优化器
# devices, num_epochs, lr, wd = d2l.try_all_gpus(), 20, 1e-4, 1e-4
# lr_period, lr_decay, net = 5, 0.5, get_net(devices)
#
#
# train(net, train_iter, valid_iter, num_epochs, lr, wd, devices, lr_period,lr_decay)
# plt.show()
#
#
