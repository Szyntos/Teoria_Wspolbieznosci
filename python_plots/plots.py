import pandas as pd
import matplotlib.pyplot as plt

# Load the CSV file into a pandas DataFrame
df = pd.read_csv('../Zadania/times2.csv')

# Group the data by function, bufferSize, and P+K
grouped = df.groupby(['function', 'bufferSize', 'P+K'])

# Plotting and saving each group's plot as a separate JPG file
functions_names = {"taketwocond": "2 conditions, consume time",
                   "puttwocond": "2 conditions, produce time",
                   "takefourcond": "4 conditions boolean, consume time",
                   "putfourcond": "4 conditions boolean, produce time",
                   "takefourcondhaswaiters": "4 conditions hasWaiters(), consume time",
                   "putfourcondhaswaiters": "4 conditions hasWaiters(), produce time",
                   "takethreelocks": "3 lock, consume time",
                   "putthreelocks": "3 lock, produce time"}
for group_name, group_data in grouped:
    function, bufferSize, pk = group_name
    plt.figure(figsize=(8, 6))
    plt.bar(group_data['idx'], group_data['time'])
    avg_time = group_data['time'].mean()
    
    # Plotting the average line
    plt.axhline(y=avg_time, color='r', linestyle='--', label='Average')
    plt.legend()
    plt.title(f'Function: {functions_names[function]}, Buffer Size: {bufferSize}, P{pk}+K{pk}')
    plt.xlabel('Produce / consume chunk size')
    plt.ylabel('Time [ns]')
    plt.grid(True)
    plt.text(len(group_data['idx']) * 0.1, avg_time*1.05, f'Mean: {avg_time:.2f}', color='black', ha='center')
    

    # Save the plot as a JPG file with a unique name based on the group
    file_name = f'plots2/plot_{function}_{bufferSize}_{pk}_time.jpg'
    plt.savefig(file_name)

    # Display the plot if you want to view it
    # plt.show()
    
    
df = pd.read_csv('../Zadania/executions.csv')

# Group the data by function, bufferSize, and P+K
grouped = df.groupby(['class', 'bufferSize', 'P+K'])

class_names = {    "consumertwocond": "2 conditions, consumer executions",
                   "producertwocond": "2 conditions, producer executions",
                   "consumerfourcond": "4 conditions boolean, consumer executions",
                   "producerfourcond": "4 conditions boolean, producer executions",
                   "consumerfourcondhaswaiters": "4 conditions hasWaiters(), consumer executions",
                   "producerfourcondhaswaiters": "4 conditions hasWaiters(), producer executions",
                   "consumerthreelocks": "3 lock, consumer executions",
                   "producerthreelocks": "3 lock, producer executions"}
# Plotting and saving each group's plot as a separate JPG file
for group_name, group_data in grouped:
    function, bufferSize, pk = group_name
    plt.figure(figsize=(8, 6))
    plt.bar(group_data['idx'], group_data['executions'])
    plt.title(f'{class_names[function]} Buffer Size: {bufferSize}, P{pk}+K{pk}')
    avg_executions = group_data['executions'].mean()
    
    # Plotting the average line
    plt.axhline(y=avg_executions, color='r', linestyle='--', label='Average')

    plt.xlabel(f'{function} id')
    plt.ylabel('Time [ns]')
    plt.grid(True)
    plt.legend()
    plt.text(len(group_data['idx']) * 0.1, avg_executions*1.05, f'Mean: {avg_executions:.2f}', color='black', ha='center')
    
    # Save the plot as a JPG file with a unique name based on the group
    file_name = f'plots2/plot_{function}_{bufferSize}_{pk}_executions.jpg'
    plt.savefig(file_name)

    # Display the plot if you want to view it
    # plt.show()