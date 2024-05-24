import os
dirs = [d[:-3] for d in os.listdir('./src/api') if (d.endswith('.py') and not d.startswith("__"))]
__all__ = dirs