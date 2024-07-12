import numpy as np
import matplotlib.pyplot as plt

#data
x = np.array([4.0, 4.2, 4.5, 4.7, 5.1, 5.5, 5.9, 6.3, 6.8, 7.1])
y = np.array([102.56, 113.18, 130.11, 142.05, 167.53, 195.14, 224.87, 256.73, 299.50, 326.72])

# Take the natural logarithm of y
ln_y = np.log(y)

# Method: Linear regression using matrices
# Construct the X matrix
X = np.column_stack((np.ones_like(x), x))

# Construct the Y matrix
Y = np.array([ln_y]).T

# Find the coefficients
coeffs = np.linalg.inv(X.T.dot(X)).dot(X.T).dot(Y)

# Convert the coefficients to a and b
a = coeffs[1, 0]
ln_b = coeffs[0, 0]

# Convert ln_b back to b
b = np.exp(ln_b)

# Print the equation of the curve
print(f"Using matrices, the equation of the curve is: y = {b:.4f} * e^({a:.4f}x)")

# Plot the data and the curve (matrices)
plt.scatter(x, y, label='Data')
plt.plot(x, b * np.exp(a * x), label='Curve')
plt.legend()
plt.show()
