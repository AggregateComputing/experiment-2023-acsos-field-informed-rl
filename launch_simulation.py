import os
import sys
import shutil
from pathlib2 import Path
from multiprocessing import Process
# Get the file passed from the command line
simulation = [
    #"simulationCentralisedLearningGNN",
    #"simulationCentralisedLearningGNNBasic",
    "simulationCentralisedLearningMLP"
]
to_replace = """
  random: &random
    min: 0
    max: 0
    step: 1
    default: 0"""

def create_net_random_section(seed):
    return """
  random: &random
    min: {seed}
    max: {seed}
    step: 1
    default: {seed}""".format(seed=seed)

# Prepare the file to be copied net file
yaml_fold = os.getcwd() + "/src/main/yaml/"
os.environ['CUBLAS_WORKSPACE_CONFIG'] = ":4096:8"
def task(name):
    os.system("./gradlew runSimulation -Pbatch=true")

for file in simulation:
    file = os.path.join(yaml_fold, file + ".yml")
    new_file = os.path.join(yaml_fold, "simulation.yml")

    for i in range(1, 2):
        # Copy the file to a new file using os
        shutil.copyfile(file, new_file)

        # Replace the random section
        path = Path(new_file)
        text = path.read_text()
        text = text.replace(to_replace, create_net_random_section(i))
        path.write_text(text)
        # Upper case only the first parameter of file
        # Launch the command simulation, and wait for it to finish
        process = Process(target=task, args=("./gradlew runSimulation -Pbatch=true",))
        process.start()
        process.join()
        process.kill()
        print("done with seed: " + str(i) + " for file: " + file + "\n")
