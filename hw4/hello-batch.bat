#!/bin/bash
#SBATCH -J myGPU           # job name
#SBATCH -o myGPU%j       # output and error file name (%j expands to jobID)
#SBATCH -N 2              # total number of nodes
#SBATCH -n 4              # total number of cores
#SBATCH -p gpu     # queue (partition) -- normal, development, etc.
#SBATCH -t 00:00:20        # run time (hh:mm:ss) - 20 seconds
#SBATCH -A EE-382C-EE-361C-Mult
#SBATCH --mail-user=christobal.sickler@gmail.com # replace by your email
#SBATCH --mail-type=begin  # email me when the job starts
#SBATCH --mail-type=end    # email me when the job finishes
./q2.out inp.txt