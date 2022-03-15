import java.lang.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Screen {
   
   Robot robot;
   DisplayMode mode;
   Dimension screenDim;
   Rectangle screen;
   Dimension boardDim;
   Rectangle board;
   HashMap<String, String> numMap;
   
   Color c1 = new Color(230, 197, 0);
   Color c2 = new Color(255, 218, 0);
   Color black = new Color(0, 0, 0);
   
   public Screen() throws Exception {
      robot = new Robot();
      mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
      screenDim = new Dimension(mode.getWidth(), mode.getHeight());
      screen = new Rectangle(screenDim);
      this.findBoard();
      
      numMap = new HashMap<String, String>();
      numMap.put("7 35", "1");
      numMap.put("8 18", "2");
      numMap.put("11 15", "3");
      numMap.put("12 13", "4");
      numMap.put("22 16", "5");
      numMap.put("23 15", "6");
      numMap.put("6 23", "7");
      numMap.put("14 14", "8");
      numMap.put("19 14", "9");
      numMap.put("0 0", ".");
   }
   
   public void findBoard() throws InterruptedException {
      BufferedImage screenShot = captureScreen();
      int screenHeight = screenDim.height;
      int screenWidth = screenDim.width;
      int[][] boardCorners = new int[2][2];
      for (int i = 0; i < screenHeight; i ++) {
         for (int j = 0; j < screenWidth; j++) {
            if (boardCorners[0][0] == 0 && boardCorners[0][1] == 0 && 
               (isColor(j, i, c1, screenShot) || isColor(j, i, c2, screenShot))) {
               boardCorners[0][0] = j;
               boardCorners[0][1] = i;
            }
         }
      }
      for (int i = 0; i < 8; i++) {
         robot.keyPress(KeyEvent.VK_RIGHT);
         robot.keyPress(KeyEvent.VK_DOWN);
      }
      Thread.sleep(100);
      screenShot = captureScreen();
      for (int i = 0; i < screenHeight; i++) {
         for (int j = 0; j < screenWidth; j++) {
            if (boardCorners[1][0] == 0 && boardCorners[1][1] == 0 &&
               (isColor(screenWidth-j-1, screenHeight-i-1, c1, screenShot) || isColor(screenWidth-j-1, screenHeight-i-1, c2, screenShot))) {
               boardCorners[1][0] = screenWidth-j-1;
               boardCorners[1][1] = screenHeight-i-1;
            }
         }
      }
      for (int i = 0; i < 8; i++) {
         robot.keyPress(KeyEvent.VK_LEFT);
         robot.keyPress(KeyEvent.VK_UP);
      }
      int boardWidth = boardCorners[1][0] - boardCorners[0][0];
      int boardHeight = boardCorners[1][1] - boardCorners[0][1];
      boardDim = new Dimension(boardWidth, boardHeight);
      board = new Rectangle(boardCorners[0][0], boardCorners[0][1], boardDim.width, boardDim.height);
   }
   
   public String readPuzzle() {
      BufferedImage board = this.captureBoard();
      int[] xs = {0, 78, 156, 240, 318, 396, 480, 558, 636};
      int[] ys = {0, 78, 156, 240, 318, 396, 480, 558, 636};
      String puzzle = "";
      for (int y : ys) {
         for (int x : xs) {
            puzzle += readSquare(board.getSubimage(x+2, y+2, 70, 70));
         }
      }
      return puzzle;
   }
   
   public String readSquare(BufferedImage image) {
      int width = image.getWidth();
      int height = image.getHeight();
      int horCount = 0;
      int vertCount = 0;
      
      for (int i = 0; i < width; i++)
         if (isColor(i, height/2, black, image))
            horCount++;
      for (int i = 0; i < height; i++)
         if (isColor(width/2, i, black, image))
            vertCount++;
      String key = horCount + " " + vertCount;      
      return numMap.get(key);
   }
   
   public boolean isColor(int x, int y, Color c, BufferedImage image) {
      Color pixel = new Color(image.getRGB(x,y));
      return c.equals(pixel);
   }
   
   public BufferedImage captureScreen() {
      return robot.createScreenCapture(screen);
   }
   
   public BufferedImage captureBoard() {
      return robot.createScreenCapture(board);
   }

   public Dimension getBoardDim() {
      return boardDim;
   }
   
   public void completePuzzle(String puzzle, String result) throws InterruptedException {
      int cursor = 0;
      while(cursor < 81) {
         if (puzzle.charAt(cursor) == '.') {
            robot.keyPress(KeyEvent.VK_1 + Character.getNumericValue(result.charAt(cursor)) - 1);
            Thread.sleep(8);
         }
         if (cursor % 18 == 8 || cursor % 18 == 9) {
            robot.keyPress(KeyEvent.VK_DOWN);
            cursor += 9;
         }
         else if (cursor % 18 < 8) {
            robot.keyPress(KeyEvent.VK_RIGHT);
            cursor += 1;
         }
         else {
            robot.keyPress(KeyEvent.VK_LEFT);
            cursor -= 1;
         }
         Thread.sleep(8);
      }
      for (int i = 0; i < 8; i++) {
         robot.keyPress(KeyEvent.VK_LEFT);
         robot.keyPress(KeyEvent.VK_UP);
      }
   }
   
   public void reset() throws InterruptedException {
      int cursor = 0;
      while(cursor < 81) {
         robot.keyPress(KeyEvent.VK_DELETE);
         Thread.sleep(10);
         if (cursor % 18 == 8 || cursor % 18 == 9) {
            robot.keyPress(KeyEvent.VK_DOWN);
            cursor += 9;
         }
         else if (cursor % 18 < 8) {
            robot.keyPress(KeyEvent.VK_RIGHT);
            cursor += 1;
         }
         else {
            robot.keyPress(KeyEvent.VK_LEFT);
            cursor -= 1;
         }
         Thread.sleep(10);
      }
      for (int i = 0; i < 8; i++) {
         robot.keyPress(KeyEvent.VK_LEFT);
         robot.keyPress(KeyEvent.VK_UP);
      }
   }

}