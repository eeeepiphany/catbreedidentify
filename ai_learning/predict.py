import collections
import math
import os
import shutil
import sys

import pandas as pd
import torch
import torchvision
from torch import nn
from d2l import torch as d2l
import matplotlib.pyplot as plt
import numpy as np
from PIL import Image


# 在Python脚本中添加
try:


    # 1. 加载模型
    net = torch.load('D:\\workspace_developer\\workspace_pycharm\\ai_learning\\complete_model.pth')

    # 2. 设置设备
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    net = net.to(device)

    # 3. 准备数据
    def transform_image(image_path):
        # ""“将图像转换为模型可以处理的格式”""
        image = Image.open(image_path).convert('RGB')
        image = torchvision.transforms.Resize(256)(image)
        image = torchvision.transforms.CenterCrop(224)(image)
        image = torchvision.transforms.ToTensor()(image)
        image = torchvision.transforms.Normalize([0.485, 0.456, 0.406],
                                                  [0.229, 0.224, 0.225])(image)
        return image


    # 获取命令行参数中的图片路径
    if len(sys.argv) > 1:
        image_path = sys.argv[1]
    else:
        print("Usage: python script.py <image_path>")
        sys.exit(1)
    image = transform_image(image_path)
    image = image.unsqueeze(0).to(device)  # 增加一个批次维度，因为模型是批处理的




    # 4. 进行预测
    net.eval()  # 设置模型为评估模式
    with torch.no_grad():
        outputs = net(image)

    # 5. 解释结果
    threshold, predicted = torch.max(outputs, 1)

    class_names = [
        "Abyssinian",
        "American Bobtail",
        "American Curl",
        "American Shorthair",
        "American Wirehair",
        "Applehead Siamese",
        "Balinese",
        "Bengal",
        "Birman",
        "Bombay",
        "British Shorthair",
        "Burmese",
        "Burmilla",
        "Calico",
        "Canadian Hairless",
        "Chartreux",
        "Chausie",
        "Chinchilla",
        "Cornish Rex",
        "Cymric",
        "Devon Rex",
        "Dilute Calico",
        "Dilute Tortoiseshell",
        "Domestic Long Hair",
        "Domestic Medium Hair",
        "Domestic Short Hair",
        "Egyptian Mau",
        "Exotic Shorthair",
        "Extra-Toes Cat - Hemingway Polydactyl",
        "Havana",
        "Himalayan",
        "Japanese Bobtail",
        "Javanese",
        "Korat",
        "LaPerm",
        "Maine Coon",
        "Manx",
        "Munchkin",
        "Nebelung",
        "Norwegian Forest Cat",
        "Ocicat",
        "Oriental Long Hair",
        "Oriental Short Hair",
        "Oriental Tabby",
        "Persian",
        "Pixiebob",
        "Ragamuffin",
        "Ragdoll",
        "Russian Blue",
        "Scottish Fold",
        "Selkirk Rex",
        "Siamese",
        "Siberian",
        "Silver",
        "Singapura",
        "Snowshoe",
        "Somali",
        "Sphynx - Hairless Cat",
        "Tabby",
        "Tiger",
        "Tonkinese",
        "Torbie",
        "Tortoiseshell",
        "Turkish Angora",
        "Turkish Van",
        "Tuxedo",
        "York Chocolate"
    ]



    #  6. 阈值判断
    if threshold > 3:
        # print(threshold)
        print(class_names[predicted.item()])
    else:
        print("The image entered is not a cat picture")


except Exception as e:
    print(f"An error occurred: {e}")
    sys.exit(1)