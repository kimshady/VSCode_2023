import numpy as np
import matplotlib.pyplot as plt

# Define the function
def f(x, y):
    return x**2 + y

# Define the range of x values
x_range = np.linspace(-2, 3, 1000)

# Define the initial conditions
y_1 = [-2]
y_2 = [-2.1]
y_3 = [-1.9]

# Define the step size
h = x_range[1] - x_range[0]

# Solve the differential equation for the initial conditions using Euler's method
for i in range(1, len(x_range)):
    y_1.append(y_1[-1] + h * f(x_range[i-1], y_1[-1]))
    y_2.append(y_2[-1] + h * f(x_range[i-1], y_2[-1]))
    y_3.append(y_3[-1] + h * f(x_range[i-1], y_3[-1]))


# Define the x and y ranges
x = np.arange(-4, 4, 1)
y = np.arange(-30, 10, 1)

# Create a meshgrid of x and y
X, Y = np.meshgrid(x, y)

# Calculate the slope at each point
u = np.ones_like(X)
v = f(X, Y)

# Plot the directional field
plt.figure(figsize=(10,6))
plt.quiver(X , Y , u , v , color ="teal" , scale =1.5, width = .05, units = 'xy' )

# Draw the curves
plt.plot(x_range, y_1, label='y(-2)=-2')
plt.plot(x_range, y_2, label='y(-2)=-2.1')
plt.plot(x_range, y_3, label='y(-2)=-1.9')
plt.xlabel('x')
plt.ylabel('y')
plt.legend()
plt.show()
