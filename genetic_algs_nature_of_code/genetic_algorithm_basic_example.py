import string
import random

"""
@author: Tristin Glunt
@date: 11/10/18
@purpose: Learning about Genetic Algorithms, implementing simple string matcher.
Inspired by Daniel Schiffman's tutorials:
https://www.youtube.com/watch?v=-jv3CgDN9sc&list=PLRqwX-V7Uu6bw4n02JP28QDuUdNi3EXxJ&index=5
"""
def main():

    # initialize vars needed for GA and this problem
    target = "i'll be back, baby."
    population_size = 600
    mutation_rate = 0.05
    current_generation = 0
    finished = 0

    # generate an initial population
    population = generate_initial_population(target, population_size, mutation_rate)
    print(population)

    fitness_scores = calculate_fitness(population, target)
    print(fitness_scores)


    begin_genetic_algorithm(population, mutation_rate, fitness_scores, current_generation, finished, target)

def generate_random_string(string_size = 10):
    # generate string of ascii characters, a-z + '!@#$%^&*()_'
    characters = string.ascii_lowercase + ",.?'!@#$%^&*() "
    # get random characters of length string_size, will return a list of characters
    random_list_of_chars = [random.choice(characters) for _ in range(string_size)]
    # join the characters together to make a string
    random_string = ''.join(random_list_of_chars)

    return random_string

def generate_initial_population(target, population_size, mutation_rate):

    population = []

    # for string proble, get length of target string
    len_of_target = len(target)

    # generate guesses for targets for the size of the population
    for i in range(population_size):
        random_string = generate_random_string(len_of_target)
        population.append(random_string)

    return population

"""
@calculate_fitness: A function that will always have to be written per problem space. for
our problem space, the fitness for each entity in the population will be the amount of
characters the current entity has correct compared to the target string
"""
def calculate_fitness(population, target):

    fitness_scores = []
    population_size = len(population)
    target_size = len(target)
    fitness_score = 0

    # print(population_size)

    for i in range(population_size):
        current_entity = population[i]

        for char in range(target_size):
            if current_entity[char] == target[char]:
                fitness_score += 1
        # append the fitness score for the current entity
        fitness_score = fitness_score / target_size
        fitness_scores.append(fitness_score)
        # reset score
        fitness_score = 0

    return fitness_scores

def begin_genetic_algorithm(population, mutation_rate, fitness_scores, current_generation, finished, target):

    # generate the pool of possible parents to select from, better fitness parents have
    # a higher probabiltity to choose
    mating_pool = generate_mating_pool(population, fitness_scores)

    # create a new generation based on the mating pool
    new_generation = generate_new_generation(mating_pool, len(population), mutation_rate)
    current_generation += 1


    fitness_scores = calculate_fitness(new_generation, target)

    finished, current_string = get_best_fitness_score(new_generation, fitness_scores, target)
    print("Generation: " + str(current_generation) + "Best string: " + current_string)

    if finished:
        print("Done. Completed on Generation: " + str(current_generation))
    else:
        begin_genetic_algorithm(new_generation, mutation_rate, fitness_scores, current_generation, finished, target)


"""
@generate_mating_pool: Based on the current fitness out of the maximum fitness, we will add
that entity to the mating pool that number of times.
EX: Fitness score = 15, maxFitness = 60, normalized_fitness = 0.25, 0.25 * 100 = 25, thus
there will be 25 adds of this entity. Therefore, the higher the fitness score, the more
likely that entity is to be a parent
"""
def generate_mating_pool(population, fitness_scores):

    mating_pool = []

    # calculate max fitness in population
    maxFitness = max(fitness_scores)
    minFiness = min(fitness_scores)

    # chance to add a bad fitness score multiple times
    chance_to_add_bad_fitness_score = 0.5

    # normalize current fitness (converting current fitness to be between 0 and 1)
    for i in range(len(population)):

        normalized_fitness = fitness_scores[i] / maxFitness
        num_times_to_add = int(normalized_fitness * 100)

        # add some randomization feature to sometimes add more bad fitness entities
        # NOTE: this add on was a big deal, converging much more often and haven't gone
        # over max recursion depth. Instead go to 200-300 gens when it's not figured out
        # in first 100.
        if fitness_scores[i] == minFiness:
            ran_num = random.uniform(0, 1)
            if ran_num < chance_to_add_bad_fitness_score:
                num_times_to_add += 10

        # add this current entity some n times to the mating pool
        for j in range(num_times_to_add):
            mating_pool.append(population[i])


    return mating_pool

def generate_new_generation(mating_pool, size_of_pop, mutation_rate):

    new_generation = []

    for i in range(size_of_pop):
        parent_a = random.choice(mating_pool)
        parent_b = random.choice(mating_pool)
        #print(parent_a, parent_b)
        child = crossover(parent_a, parent_b)
        child = mutate(child, mutation_rate)
        new_generation.append(child)

    return new_generation

def crossover(parent_a, parent_b):

    len_of_child = len(parent_a)
    # print(len_of_child)
    cutting_point = random.randint(0, len_of_child)
    # print("half of child " + str(half_of_child))

    first_half, second_half = parent_a[:cutting_point], parent_b[cutting_point:]

    child = first_half + second_half

    return child

def mutate(child, mutation_rate):

    ran_num = random.uniform(0, 1)
    child = list(child)
    for i in range(len(child)):
        if ran_num <= mutation_rate:
            child[i] = chr(random.randint(97, 122))

    child = "".join(child)
    return child

def get_best_fitness_score(population, fitness_scores, target):

    finished = False

    # get the max fitness score index to index into the string that's related
    best_string = population[fitness_scores.index(max(fitness_scores))]

    if best_string == target:
        finished = True
    else:
        finished = False

    return finished, best_string

"""
TODO:

- Calculate avg fitness of Generation
- Show analysis of X amt of runs:
    -- avg convergence rate
    -- number of fails
    -- number of successes


"""
if __name__ == "__main__":
    main()
