import pandas as pd
import matplotlib.pyplot as plt
import os
import decimal

def makePlots(path, plot_type):
    data = pd.read_csv(path)

    if not os.path.exists('plots'):
        os.makedirs('plots')

    buffer_groups = data.groupby('bufferType')

    for buffer_type, buffer_data in buffer_groups:
        # Create a directory for each bufferType if it doesn't exist
        buffer_folder = os.path.join('plots', buffer_type)
        if not os.path.exists(buffer_folder):
            os.makedirs(buffer_folder)

        # Create subdirectories for 'times' and 'execs'
        if not os.path.exists(os.path.join(buffer_folder, plot_type)):
            os.makedirs(os.path.join(buffer_folder, plot_type))

        # Group data within bufferType groups by other columns
        unique_combinations = buffer_data.groupby(['bufferType', 'rngType', 'function', 'bufferSize', 'prodConsCount'])

        for group, df_group in unique_combinations:
            s = "Number of Executions of " if plot_type == "executions" else "Mean Time per Chunk of "
            plt.figure(figsize=(8, 6))  # Adjust figure size as needed
            plt.title(f"{s}{group[0]} with {group[1]} - {group[2].upper()}")

            # Plot bar chart based on plot_type argument
            if plot_type == 'executions':
                plt.bar(df_group['threadID'], df_group['executions'], color=('blue' if group[2] == "take" else "red"))
                plt.ylabel('Number of Executions')
                plt.xlabel('Thread ID')
                mean_value = df_group['executions'].mean()
                print(group, df_group['executions'].sum())
                plt.text(df_group['threadID'].iloc[-1], mean_value, f'Mean: {mean_value:.3E}', va='center',
                         ha='right', backgroundcolor='white')

            else:
                plt.bar(df_group['putTakeChunk'], df_group['meanTime'], color=('blue' if group[2] == "take" else "red"))
                plt.ylabel('Mean Time per Chunk [ns]')
                plt.xlabel('Processed Chunk')
                mean_value = df_group['meanTime'].mean()
                # print(group, df_group['meanTime'].sum())
                plt.text(df_group['putTakeChunk'].iloc[-1], mean_value, f'Mean: {mean_value:.3E} [ns]', va='center',
                         ha='right', backgroundcolor='white')

            # Calculate mean and plot mean line

            plt.axhline(mean_value, color='green', linestyle='--', label=f'Mean')


            plt.grid(True)
            plt.legend(loc='lower left')

            # Save plot to respective bufferType folder
            plot_filename = f"{group[0]}_{group[1]}_{group[2][0]}_{plot_type}_plot.png"
            plot_path = os.path.join(buffer_folder, plot_type, plot_filename)
            plt.tight_layout()
            plt.savefig(plot_path)
            plt.close()


if __name__ == "__main__":
    makePlots('../Zadania/times.csv', "times")
    makePlots('../Zadania/executions.csv', "executions")
