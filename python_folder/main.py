# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

import pandas as pd
import numpy as np


def print_hi(name):
    f=open("node_reward.txt","w")
    # Use a breakpoint in the code line below to debug your script.
    dataframe = pd.read_csv("D:\\Untitled.csv")
    delay_mean=dataframe[(dataframe["name"] == "endToEndDelay:mean") & (dataframe["type"]=="scalar")]
    delay_mean=delay_mean[["module","name","value"]].reset_index(drop=True)
    delay_mean_array=[]
    for i in range(delay_mean.shape[0]):
        series_temp=delay_mean.iloc[i]
        node=(series_temp["module"][series_temp["module"].find("[")+1:series_temp["module"].find("]")])
        value=(series_temp["value"])
        delay_mean_array.append([node,value])

    # print(delay_mean_array)
    f.write("delay_mean\n")
    for i in range(len(delay_mean_array)):
        f.write(str(delay_mean_array[i][0])+":"+str(delay_mean_array[i][1]))
        f.write("\n")
    # 有的数据有routing，要去掉
    drop_sum = dataframe[(dataframe["name"] == "drop:sum") & (dataframe["type"] == "scalar")]
    drop_sum=drop_sum[["module","name","value"]].reset_index(drop=True)
    drop_sum_array=[] #node, value
    drop_sum_array_temp=[] #node, node_queue, value
    for i in range(drop_sum.shape[0]):
        series_temp = drop_sum.iloc[i]
        if "routing" in series_temp["module"]:
            continue
        first_left_bracket_pos=series_temp["module"].find("[")
        first_right_bracket_pos=series_temp["module"].find("]")
        second_left_bracket_pos=series_temp["module"].find("[",first_left_bracket_pos+1)
        second_right_bracket_pos = series_temp["module"].find("]", first_right_bracket_pos + 1)
        node=(series_temp["module"][first_left_bracket_pos+1:first_right_bracket_pos])
        node_queue=series_temp["module"][second_left_bracket_pos+1:second_right_bracket_pos]
        value=series_temp["value"]
        # print(str(node)+"   "+str(node_queue)+"  "+str(value))
        drop_sum_array_temp.append([int(node),int(node_queue),value])
    #下面两个是临时变量
    node_i=0
    temp_value=0
    for i in range(len(drop_sum_array_temp)):
        if drop_sum_array_temp[i][0]==node_i: #如果还是当前节点的队列
            temp_value+=drop_sum_array_temp[i][2]
        else: #如果到了下一个节点的队列
            drop_sum_array.append([node_i,temp_value])
            node_i = drop_sum_array_temp[i][0]
            temp_value=0
    #退出循环的时候，最后一个节点还没有统计
    drop_sum_array.append([node_i, temp_value])
    # print(drop_sum_array)
    f.write("drop_sum\n")
    for i in range(len(drop_sum_array)):
        f.write(str(drop_sum_array[i][0])+":"+str(drop_sum_array[i][1]))
        f.write("\n")

    delay_max = dataframe[(dataframe["name"] == "endToEndDelay:max") & (dataframe["type"] == "scalar")]
    delay_max = delay_max[["module", "name", "value"]].reset_index(drop=True)
    delay_max_array = []
    for i in range(delay_max.shape[0]):
        series_temp = delay_max.iloc[i]
        node = (series_temp["module"][series_temp["module"].find("[") + 1:series_temp["module"].find("]")])
        value = (series_temp["value"])
        delay_max_array.append([int(node), value])
    print(delay_max_array)
    delay_min = dataframe[(dataframe["name"] == "endToEndDelay:min") & (dataframe["type"] == "scalar")]
    delay_min = delay_min[["module", "name", "value"]].reset_index(drop=True)
    delay_min_array = []
    for i in range(delay_min.shape[0]):
        series_temp = delay_min.iloc[i]
        node = (series_temp["module"][series_temp["module"].find("[") + 1:series_temp["module"].find("]")])
        value = (series_temp["value"])
        delay_min_array.append([int(node), value])
    print(delay_min_array)
    delay_shake=[]
    for i in range(len(delay_max_array)):
        delay_shake.append([delay_max_array[i][0],delay_max_array[i][1]-delay_min_array[i][1]])
    print(delay_shake)
    f.write("delay_shake\n")
    for i in range(len(delay_shake)):
        f.write(str(delay_shake[i][0])+":"+str(delay_shake[i][1]))
        f.write("\n")

    f.close()

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
