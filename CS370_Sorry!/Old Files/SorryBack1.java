import java.util.Scanner;

class Sorry{
	public static void main(String[] args){
		Game myGame = new Game();
		
	}
}

class Game{
	GameBoard gb = new GameBoard();
	int turn;
	int players;
	Scanner keyboard = new Scanner(System.in);
	
	public Game(){
		turn = 0;
		players = 4;
		
		//Temp Populate
		gb.newPawn(1,Color.RED);
		gb.newPawn(2,Color.YELLOW);
		gb.newPawn(4,Color.GREEN);
		gb.newPawn(8,Color.BLUE);
		gb.newPawn(16,Color.RED);
		gb.newPawn(32,Color.YELLOW);
		gb.newPawn(5,Color.GREEN);	
		gb.newPawn(10,Color.BLUE);
		gb.newPawn(20,Color.RED);
		gb.newPawn(40,Color.YELLOW);
		gb.newPawn(21,Color.GREEN);
		gb.newPawn(42,Color.BLUE);
		gb.newPawn(24,Color.RED);
		gb.newPawn(48,Color.YELLOW);
		gb.newPawn(36,Color.GREEN);
		gb.newPawn(12,Color.BLUE);

		while(turn < players){
			doTurn();
		}
	}

	public void nextTurn(){
		turn = (turn+1)%players;
	}

	public int selectPawn(){
		int selection = -1;
		
		while(selection >= 60 || selection < 0){
			System.out.println("Enter the piece you want to move: ");
			selection = keyboard.nextInt();
			if(selection >= 60 || selection < 0){
				System.out.println("Invalid range. Try again.");
			}else if(gb.spaces[selection] == null){
				System.out.println("Invalid. Space was null. Try again.");
				selection = -1;
			}else if(gb.spaces[selection].myColor != Color.values()[turn]){
				System.out.println("Invalid. Color does not match player. Try again.");
				selection = -1;
			}
		}

		return selection;
	}

	public void doTurn(){
		int space;

		System.out.print("\n");
		gb.showBoard();
		System.out.print("\n");
		System.out.println(Color.values()[turn]+"'s Turn");
		space = selectPawn();
		gb.movePawn(space, 1);

		nextTurn();
	}
}

enum Color{
	RED, YELLOW, GREEN, BLUE;
}

class Pawn{
	Color myColor;

	public Pawn(Color c){
		myColor = c;
	}

	@Override
	public String toString(){
		return myColor+" Pawn";
	}
}

class GameBoard{
	Pawn[] spaces;
	Pawn[] redSafe;
	Pawn[] yellowSafe;
	Pawn[] greenSafe;
	Pawn[] blueSafe;
	int redStart, yellowStart, greenStart, blueStart;
	int redHome, yellowHome, greenHome, blueHome;

	public GameBoard(){
		spaces = new Pawn[60];
		redSafe = new Pawn[5];
		yellowSafe = new Pawn[5];
		greenSafe = new Pawn[5];
		blueSafe = new Pawn[5];
	}

	public void newPawn(int index, Color c){
		spaces[index] = new Pawn(c);
	}

	public void movePawn(int index, int amt){
		spaces[(index+amt)%60] = spaces[index];
		spaces[index] = null;
	}

	public void showBoard(){
		for(int i = 0; i < 60; i++){
			System.out.println(i+": "+spaces[i]);
		}
		for(int i = 0; i < 5; i++){
			System.out.println("R"+i+": "+redSafe[i]);
		}
		for(int i = 0; i < 5; i++){
			System.out.println("Y"+i+": "+yellowSafe[i]);
		}
		for(int i = 0; i < 5; i++){
			System.out.println("G"+i+": "+greenSafe[i]);
		}
		for(int i = 0; i < 5; i++){
			System.out.println("B"+i+": "+blueSafe[i]);
		}
	}
}
