import time
import copy

r3 = range(3)
r9 = range(9)
r81 = range(81)

row_groups = [[9*j+i for i in r9] for j in r9]
column_groups = [[9*i+j for i in r9] for j in r9]
block_groups = [[27*j+3*i+9*l+k for k in r3 for l in r3] for i in r3 for j in r3]
constraint_groups = row_groups + column_groups + block_groups

def constraint_propagation(board):
   constrained = False
   for group in constraint_groups:  
      for idx in group:
         if len(board[idx]) == 1:
            for neighbor in group:
               if idx != neighbor and board[idx] in board[neighbor]:
                  board[neighbor] = board[neighbor].replace(board[idx], '')
                  constrained = True
      for d in '123456789':
         pos = []
         for idx in group:
            if d == board[idx]:
               break
            if d in board[idx] and len(board[idx]) > 1:
               pos.append(idx)
         else:
            if len(pos) == 1:
               board[pos[0]] = d
   return constrained

def forward_pass(board):
   constrain = True
   while(constraint_propagation(board)):
      solved = {k for k in r81 if len(board[k]) == 1}
   if len(solved) == 81:
      return board
   if 0 in [len(k) for k in board]:
      return None
   n, idx = min([(len(board[k]), k) for k in r81 if len(board[k]) > 1])
   for k in board[idx]:
      new_board = copy.copy(board)
      new_board[idx] = k
      attempt = forward_pass(new_board)
      if attempt != None:
         return attempt
   return None

def solve(puzzle):
   if len(puzzle) != 81:
      print('Error: puzzle length is ' + str(len(puzzle)))
      return
   board = [puzzle[k] for k in r81]
   print(format_board(board))
   print('')
   for k in r81:
      if board[k] == '.':
         board[k] = '123456789'
   board = forward_pass(board)
   if board == None:
      print('No solution')
   else:
      print(format_board(board))
   print('')
   return ''.join(board)

def format_board(board):
   return '\n\n'.join(['\n'.join([format_row(board[27*j+9*i:27*j+9*i+9]) for i in r3]) for j in r3])

def format_row(row):
   return '\t\t'.join(['\t'.join([str(row[3*j+i]) for i in r3]) for j in r3])
   
# if __name__ == "__main__":
#    puzzle = '3.....289.7.......9....47.....5...7...16...3.....21....46.8...........46..83...5.'
#    
#    screen.readPuzzle();  
#    start = time.time()
#    solve(puzzle)
#    print(time.time() - start)