# def account_create(initial_amount=0):
#     def atm(num, deposit=True):        # 定义闭包
#         nonlocal initial_amount
#         if deposit:
#             initial_amount += num
#             print(f"存款：+{num}，账户余额：{initial_amount}")
#         else:
#             initial_amount -= num
#             print(f"取款：-{num}，账户余额：{initial_amount}")
#     return atm        # 不要写成 return atm()
#
#
# fn = account_create(1000)           # fn的类型是一个函数对象，即一个可调用的函数，且fn是function的缩写
# fn(100, False)
# fn(9, True)