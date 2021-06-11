import com.qqwing.QQWing;

public class MainGA {

	public static void main(String[] args) throws Exception {
		
		boolean anEasyPuzzle = true;
		
		if(args.length == 1 && args[0].equalsIgnoreCase("-r")) {
			mainExecution(anEasyPuzzle);
		} else if (args.length == 3 && args[0].equalsIgnoreCase("-s")) {
			SudokuGa.setMaxAllowedEvolutions(Integer.parseInt(args[1]));
			SudokuGa.setMaxAllowedPopulation(Integer.parseInt(args[2]));
			mainExecution(anEasyPuzzle);
		} else if (args.length == 4 && args[0].equalsIgnoreCase("-s") && (args[3].equalsIgnoreCase("true"))) {
			SudokuGa.setMaxAllowedEvolutions(Integer.parseInt(args[1]));
			SudokuGa.setMaxAllowedPopulation(Integer.parseInt(args[2]));
			mainExecution(anEasyPuzzle);
		} else if (args.length == 4 && args[0].equalsIgnoreCase("-s") && (args[3].equalsIgnoreCase("false"))) {
			SudokuGa.setMaxAllowedEvolutions(Integer.parseInt(args[1]));
			SudokuGa.setMaxAllowedPopulation(Integer.parseInt(args[2]));
			mainExecution(!anEasyPuzzle);
		} else {
			System.out.println("The input is not valid:");
    		System.out.println("-r: It solves a sudoku using the default values for evolutions and population");
    		System.out.println("-s <max_number_of_evolutions> <max_population_allowed>: It solves a sudoku using the taking into account the maximum number of evolutions and population");
    		System.out.println("-s <max_number_of_evolutions> <max_population_allowed> <easy>: It solves a sudoku with the maximum number of evolutions and population parameters and takes into account if the sudoku is easy ('true' or 'false')");
		}
	}
	
	private static void mainExecution(boolean anEasyPuzzle) throws Exception {
        QQWing MySudoku = new QQWing();
        MySudoku.generatePuzzle();
        if (anEasyPuzzle) { //in case if the user wants to solve an easy sudoku
            System.out.println("Easy puzzle has been selected");
            int[] easyPuzzle = {6, 2, 0, 3, 9, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 2, 9, 0, 1, 0, 8, 0, 6, 0, 0, 0, 0, 4, 0, 2, 0, 0, 8, 9, 0, 0, 0, 0, 0, 9, 0, 1, 4, 0, 2, 3, 1, 0, 0, 7, 0, 0, 0, 8, 9, 0, 0, 0, 2, 3, 8, 0, 5, 2, 6, 0, 5, 8, 0, 3, 0, 0, 8, 3, 0, 0, 0, 0, 1, 2, 9};
            MySudoku.setPuzzle(easyPuzzle);
        }
        MySudoku.printPuzzle();
        int[] initialPuzzle = MySudoku.getPuzzle();
        SudokuGa.findSolution(MySudoku);
        System.out.println("Real solution");
        MySudoku.setPuzzle(initialPuzzle);
        MySudoku.solve();
        MySudoku.printSolution();
	}

}
