def Mystery1(n, b):
    """Return the number of times n can be divided by b."""
    if n < b: 
        return str(n)
    return Mystery1(n // b, b) + Mystery1(n % b, b)