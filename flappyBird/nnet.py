import numpy as np

def sigmoid(Z):
    return 1 / (1 + np.exp(-Z))

class NeuralNetwork(object):
    def __init__(self, layer_dims):
        self.layer_dims = layer_dims

    def initialize_parameters(self, layer_dims):
        """
        parameters will be a dictionary, each layer contains a weight matrix
        with a weight for each input to every neuron (current_layer, previous layer) (4 neurons, 3 inputs)
        each neuron has 3 weights going to it. each neuron also has a bias initialized to 0
        """
        parameters = {}
        L = len(layer_dims)

        for l in range(1, L):
            parameters["W" + str(l)] = np.random.randn(layer_dims[l], layer_dims[l-1])*0.01
            parameters["b" + str(l)] = np.zeros([layer_dims[l], 1]) #needs to be wrapped in a list

        return parameters

    def forward_propagation(self, X, parameters):
        counter = 0
        A = X
        L = len(parameters) // 2 #important! we only need to loop through half as many parameters there are since W & b, double slash is int divide
        activation_function = "sigmoid"

        for l in (1, L):
            counter+=1
            print(counter)
            A_prev = A #get the last activation output (the inputs for the current layer)
            W = parameters["W" + str(l)]
            print("W shape: " + str(W.shape))
            b = parameters["b" + str(l)]
            Z = np.dot(W, A_prev) + b #linear function

            if(activation_function == "sigmoid"):
                A = sigmoid(Z)
            elif(activation_function == "relu"):
                pass

        #Z_hat = np.dot(parameters["W" + str(L)], A) + parameters["b" + str(L)]
        #y_hat = sigmoid(Z_hat)

        return A

    def train(self, X, Y, num_iterations):
        """
        train will implemenet cost functions and back propagation to adjust
        weights
        """


inputs = np.array([1, 2, 3, 4]).reshape(-1,1)
print("inputs shape: " + str(inputs.shape))
nnet_dims = [4, 4, 1]

brain = NeuralNetwork(nnet_dims)
parameters = brain.initialize_parameters(brain.layer_dims)
print(parameters)
output = brain.forward_propagation(inputs, parameters)
print(output)
