import numpy as np
import matplotlib.pyplot as plt

# Define the differential equation
def dydx(y, x):
    return 3 - y + 1/x

# Define the range of x and y values
x = np.linspace(-10, 10, 20)
y = np.linspace(-10, 10, 20)

# Create a grid of x and y values
X, Y = np.meshgrid(x, y)

# Calculate the slope at each point
U = 1
V = dydx(Y, X)

M = np.sqrt(U**2 + V**2)

# Table title and labels
plt.xlabel('x')
plt.ylabel('y')
plt.title('Directional Field of dy/dx = 3 - y + 1/x')

# Plot
#plt.quiver(X, Y, U/M, V/M, color='black', scale=30)




plt.show()
