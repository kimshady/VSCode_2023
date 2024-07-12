def decode_message(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    # Sorting based on the numerical values
    sorted_lines = sorted(lines, key=lambda line: int(line.split()[0]))

    decoded_message = []
    current_term = 1
    for line in sorted_lines:
        parts = line.split()

        number, word = parts
            
        numeric_part = int(number)
        term_value = (current_term * (current_term + 1)) // 2

        if numeric_part == term_value:
            decoded_message.append(word.strip())
            current_term += 1

    return ' '.join(decoded_message)

# Example usage:
file_path = 'message.txt'
decoded_message = decode_message(file_path)
print(decoded_message)
