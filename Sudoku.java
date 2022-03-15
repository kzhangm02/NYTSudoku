import org.python.util.PythonInterpreter;
import org.python.core.PyObject;
import org.python.core.PyString;

public class Sudoku {

   public static void main(String[] args) throws Exception { 
      long start = System.currentTimeMillis();
      PythonInterpreter pyInterpreter = new PythonInterpreter();
      pyInterpreter.exec("from Solver import *");
      PyObject solve = pyInterpreter.get("solve");
      long elapsed = System.currentTimeMillis() - start;
      
      Thread.sleep(Math.max(1, 3000-elapsed));
       
      Screen screen = new Screen();
      String puzzle = screen.readPuzzle();
     
      boolean reset = false;
      if (reset)
         screen.reset();
      else {
         PyObject pyResult = solve.__call__(new PyString(puzzle));
         String result = (String) pyResult.__tojava__(String.class);
         screen.completePuzzle(puzzle, result);
      }
      System.exit(0);  
   }

}