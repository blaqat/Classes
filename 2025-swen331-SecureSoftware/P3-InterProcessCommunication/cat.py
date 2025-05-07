# cat.py - a safe(?) version of cat!
# The linux command `cat` prints out the contents of a file,
# if you give it a file name to open.
#
# Our version will only open files in the directory `litterbox`
# (also, unlike cat, it takes the filename from stdin, not args)

# INPUT: from stdin, we will take in the file name and open it
# OUTPUT: to stdout, the contents of the file if it's in `litterbox/`,
#			         or MEOW if not
import sys

def main():
  filename = sys.stdin.read().strip()
  filename_in_litterbox_hopefully = f"./litterbox/{filename}"
  #                  VULNERABLE HERE^^^^^^^^^^^^^^^^^^^^^^^^
  #
  try:
    with open(filename_in_litterbox_hopefully, 'r') as f:
      print(f.read())
  except Exception as e:
    print(f"MEOW {e}");

  # the secure way would be like this:
  # (add import os to the top)
  # abs_filename = os.path.abspath(f"./litterbox/{filename}")
  # if abs_filename.startswith(os.path.abspath(f"./litterbox/")):
  # 	filename_in_litterbox_hopefully = abs_filename
  # else:
  # 	raise Exception('Sandbox escaped!')
  #
  # But this is about Defense in Depth and WHAT IF we made the above mistake
  pass

if __name__ == '__main__':
  main()
