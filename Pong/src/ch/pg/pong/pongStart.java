package ch.pg.pong;

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class pongStart extends Application {
	private static int KEYBOARD_MOVEMENT_DELTA = 5;
	private static final Duration TRANSLATE_DURATION = Duration.millis(1);

	private static boolean r11 = false, r12 = false, r21 = false, r22 = false, end = false;

	static int x = 500, y = 200, min = 3, max = 7, coutup = 0;

	private static boolean top = false, bottom = true, right = true, left = false, space = true;

	int player = 0, playercount = 0, winsbot = 0, winsplayerbot = 0, winsplayer1 = 0, winsplayer2 = 0;

	int randomNumX = 3, randomNumY = 3;

	Scene scene1, scene2, scene3;

	static Label label1, label2;
	static Button button1, button2;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * creates the objects in the Scenes.
	 * 
	 * scene1 label1, label2, button1, button2 scene2 label, circlebot, rec1bot,
	 * rec2bot, middlebot scene3 label, circle, rec1, rec2, middle
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// creates the Label and the Buttons that will show in scene1
		label1 = new Label("One Player or Two Player");
		label2 = new Label("Player one: " + winsplayer1 + "\nPlayer two: " + winsplayer2 + "\n\nBot: " + winsbot
				+ "\nPlayerBot: " + winsplayerbot);
		label2.setLayoutY(30);

		button1 = new Button("1 Player");
		button1.setLayoutX(20);
		button1.setLayoutY(140);

		button2 = new Button("2 Player");
		button2.setLayoutX(100);
		button2.setLayoutY(140);

		// for the game with the Bot
		final Circle circlebot = createCircle();
		final Rectangle rec1bot = createRectangle1();
		final Rectangle rec2bot = createRectangle2();
		final Line middlebot = createLine();

		// for the Game 2 player
		final Circle circle = createCircle();
		final Rectangle rec1 = createRectangle1();
		final Rectangle rec2 = createRectangle2();
		final Line middle = createLine();

		final Group group1 = new Group(label1, label2, button1, button2);
		final Group group2 = new Group(createInstructions1(), circlebot, rec1bot, rec2bot, middlebot);
		final Group group3 = new Group(createInstructions2(), circle, rec1, rec2, middle);

		final TranslateTransition transition1 = createTranslateTransition(circlebot, rec1bot, rec2bot, stage);
		final TranslateTransition transition2 = createTranslateTransition(circle, rec1, rec2, stage);

		// change the scene if Button1 or Button2 was pressed
		button1.setOnAction(e -> stage.setScene(scene2));
		button2.setOnAction(e -> stage.setScene(scene3));

		scene1 = new Scene(group1, 200, 200);
		scene2 = new Scene(group2, 1001, 501, Color.CORNSILK);
		scene3 = new Scene(group3, 1000, 500, Color.CORNSILK);

		moveRecOnKeyPress(scene2, rec1, rec2, circle, transition1);
		moveRecOnKeyRelease(scene2);

		moveRecOnKeyPress(scene3, rec1, rec2, circle, transition2);
		moveRecOnKeyRelease(scene3);

		stage.setScene(scene1);
		stage.setTitle("PONG");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * creates an label at the top of the Scene
	 * 
	 * @return
	 */
	private Label createInstructions1() {
		playercount = 1;
		Label instructions = new Label("Use the UP DOWN keys to move UP and DOWN. Press Space to Start");
		instructions.setTextFill(Color.FORESTGREEN);
		return instructions;
	}

	/**
	 * creates an label at the top of the Scene
	 * 
	 * @return
	 */
	private Label createInstructions2() {
		playercount = 2;
		Label instructions = new Label(
				"Use the UP DOWN keys to move the right. Use the W S Keys to Move the left. Press Space to Start");
		instructions.setTextFill(Color.FORESTGREEN);
		return instructions;
	}

	/**
	 * creates the Circle to a specific location and a Radius and color
	 * 
	 * @return
	 */
	private Circle createCircle() {
		final Circle circle = new Circle(500, 200, 20, Color.BLACK);
		return circle;
	}

	/**
	 * sets the left Rectangle with position and size and color
	 * 
	 * @return
	 */
	private Rectangle createRectangle1() {
		final Rectangle Rectangle1 = new Rectangle(20, 150, Color.BLUE);
		Rectangle1.setLayoutX(970);
		Rectangle1.setLayoutY(150);
		return Rectangle1;
	}

	/**
	 * sets the right Rectangle with position and size and color
	 * 
	 * @return
	 */
	private Rectangle createRectangle2() {
		final Rectangle Rectangle2 = new Rectangle(20, 150, Color.RED);
		Rectangle2.setLayoutX(10);
		Rectangle2.setLayoutY(150);
		return Rectangle2;
	}

	/**
	 * creates a line in the middle from X500 and Y0 to X500 and Y550
	 * 
	 * @return
	 */
	private Line createLine() {
		final Line middle = new Line(500, 0, 500, 550);
		return middle;
	}

	/**
	 * the repeat for the Rectangulars and the Circle
	 * 
	 * if its reach top or bottom it will bounce. also when the circle comes against
	 * an Rectangular, if it reach the left or the right border the opposite Person
	 * will win.
	 * 
	 * @param circle
	 * @param rec1
	 * @param rec2
	 * @param stage
	 * @return
	 */
	private TranslateTransition createTranslateTransition(final Circle circle, final Rectangle rec1,
			final Rectangle rec2, Stage stage) {
		final TranslateTransition transition = new TranslateTransition(TRANSLATE_DURATION, circle);
		transition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				// gets the Height of the active stage for knowing it is scene1 or scene2
				if (stage.getScene().getHeight() == 501) {
					playercount = 1;
				} else {
					playercount = 2;
				}
				Keymovent(rec1, rec2, circle);
				if (circle.getCenterY() <= (rec1.getLayoutY() + 150) && circle.getCenterY() >= rec1.getLayoutY()
						&& circle.getCenterX() >= 950 && right == true) {
					// Right Box bounce
					Random r = new Random();
					randomNumX = r.nextInt(max - min) + min;
					left = true;
					right = false;
					coutup++;
				} else if (circle.getCenterY() <= (rec2.getLayoutY() + 150) && circle.getCenterY() >= rec2.getLayoutY()
						&& circle.getCenterX() <= 50 && left == true) {
					// left Box bounce
					Random r = new Random();
					randomNumX = r.nextInt(max - min) + min;
					left = false;
					right = true;
					coutup++;
				} else if (circle.getCenterY() >= 480 || circle.getCenterY() <= 20) {
					if (circle.getCenterY() >= 490 && top == true) {
						// bottom bounce
						Random r = new Random();
						randomNumY = r.nextInt(max - min) + min;
						top = false;
						bottom = true;
					} else if (circle.getCenterY() <= 20 && bottom == true) {
						// Top bounce
						Random r = new Random();
						randomNumY = r.nextInt(max - min) + min;
						top = true;
						bottom = false;
					}
				} else if (circle.getCenterX() <= 5 && left == true) {
					// Left Border Player1 Win
					player = 1;
					end = true;
				} else if (circle.getCenterX() >= 1000 && right == true) {
					// Right Border Player2 Win
					player = 2;
					end = true;
				}

				// the next position for the Circle to go
				if (right == true) {
					x = x + randomNumX;
				} else if (left == true) {
					x = x - randomNumX;
				}

				// the next position for the Circle to go
				if (top == true) {
					y = y + randomNumY;
				} else if (bottom == true) {
					y = y - randomNumY;
				}

				switch (coutup) {
				// speed up the ball / circle after X bounces by the Rectangulars
				case 10:
					min = min + 1;
					max = max + 1;
					coutup++;
					break;
				case 21:
					min = min + 1;
					max = max + 1;
					coutup++;
					break;
				case 31:
					min = min + 2;
					max = max + 2;
					coutup++;
					break;
				case 41:
					min = min + 2;
					max = max + 2;
					coutup++;
					break;
				case 51:
					min = min + 3;
					max = max + 3;
					coutup++;
					break;
				case 61:
					min = min + 3;
					max = max + 3;
					coutup++;
					break;
				case 71:
					min = min + 4;
					max = max + 4;
					coutup++;
					break;
				case 81:
					min = min + 4;
					max = max + 4;
					coutup++;
					break;
				case 91:
					min = min + 5;
					max = max + 5;
					coutup++;
					break;
				}

				circle.setCenterX(circle.getTranslateX() + circle.getCenterX());
				circle.setCenterY(circle.getTranslateY() + circle.getCenterY());
				circle.setTranslateX(0);
				circle.setTranslateY(0);

				if (end == true) {
					// ends if the ball / circle touches the left or write border
					End(circle, rec1, rec2, stage);

					transition.pause();
					transition.stop();
				}
				moveCircleOnSpacePress(circle, transition);
			}
		});
		return transition;
	}

	/**
	 * gets the Key press and gets the code to true and if the Rectangular is at the
	 * top or at the Bottom it will not go further
	 *
	 * @param scene
	 * @param rec2
	 * @param rec1
	 * @param circle
	 * @param transition
	 */
	private void moveRecOnKeyPress(Scene scene, final Rectangle rec2, final Rectangle rec1, final Circle circle,
			final TranslateTransition transition) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE && space == true) {
					end = false;
					space = false;
					moveCircleOnSpacePress(circle, transition);
				}

				if (event.getCode() == KeyCode.UP) {
					if (rec1.getLayoutY() == 10) {

					} else {
						r11 = true;
					}
				}

				if (event.getCode() == KeyCode.DOWN) {
					if (rec1.getLayoutY() == 350) {

					} else {
						r12 = true;
					}
				}

				if (playercount == 2) {
					if (event.getCode() == KeyCode.W) {
						if (rec2.getLayoutY() == 10) {

						} else {
							r21 = true;
						}
					}

					if (event.getCode() == KeyCode.S) {
						if (rec2.getLayoutY() == 350) {

						} else {
							r22 = true;
						}
					}
				}
			}
		});
	}

	/**
	 * gets the Key Release and sets the code to false
	 * 
	 * @param scene
	 */
	private void moveRecOnKeyRelease(Scene scene) {
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.UP) {
					r11 = false;
				}

				if (event.getCode() == KeyCode.DOWN) {
					r12 = false;
				}

				if (playercount == 2) {
					if (event.getCode() == KeyCode.W) {
						r21 = false;
					}

					if (event.getCode() == KeyCode.S) {
						r22 = false;
					}
				}
			}
		});
	}

	/**
	 * moves the circle to the position x and y
	 * 
	 * @param circle
	 * @param transition
	 */
	private void moveCircleOnSpacePress(final Circle circle, final TranslateTransition transition) {
		transition.setToX(x - circle.getCenterX());
		transition.setToY(y - circle.getCenterY());
		if (end == false) {
			transition.playFromStart();
		} else {
			transition.stop();
		}
	}

	/**
	 * Moves the Rectangulars up or down
	 * 
	 * @param rec1
	 * @param rec2
	 * @param circle
	 */
	@FXML
	public void Keymovent(final Rectangle rec1, final Rectangle rec2, final Circle circle) {
		if (r11 == true) {
			if (rec1.getLayoutY() < 10) {

			} else {
				rec1.setLayoutY(rec1.getLayoutY() - KEYBOARD_MOVEMENT_DELTA);
				r11 = true;
			}
		}

		if (r12 == true) {
			if (rec1.getLayoutY() > 350) {

			} else {
				rec1.setLayoutY(rec1.getLayoutY() + KEYBOARD_MOVEMENT_DELTA);
				r12 = true;
			}
		}

		if (playercount == 1) {
			if (rec2.getLayoutY() + 75 > circle.getCenterY()) {
				if (rec2.getLayoutY() == 10) {

				} else {
					rec2.setLayoutY(rec2.getLayoutY() - KEYBOARD_MOVEMENT_DELTA);
				}
			}

			if (rec2.getLayoutY() + 75 < circle.getCenterY()) {
				if (rec2.getLayoutY() == 350) {

				} else {
					rec2.setLayoutY(rec2.getLayoutY() + KEYBOARD_MOVEMENT_DELTA);
				}
			}
		} else {
			if (r21 == true) {
				if (rec2.getLayoutY() < 10) {

				} else {
					rec2.setLayoutY(rec2.getLayoutY() - KEYBOARD_MOVEMENT_DELTA);
					r21 = true;
				}
			}

			if (r22 == true) {
				if (rec2.getLayoutY() > 350) {

				} else {
					rec2.setLayoutY(rec2.getLayoutY() + KEYBOARD_MOVEMENT_DELTA);
					r22 = true;
				}
			}
		}
	}

	/**
	 * Pop up at the end to now if Player 1 or Player 2 / Bot has won and Resets
	 * every thing for to restart
	 * 
	 * @param circle
	 * @param rec1
	 * @param rec2
	 * @param stage
	 */
	@FXML
	public void End(final Circle circle, final Rectangle rec1, final Rectangle rec2, Stage stage) {
		String text = null;
		if (player == 1) {
			if (stage.getScene().getHeight() == 501) {
				winsplayerbot++;
			} else {
				winsplayer1++;
			}
			text = "GG. Player 1 won! Click Ok to exit.";
		} else if (player == 2) {
			if (playercount == 1) {
				text = "GG. The Bot won! Click Ok to exit.";
				winsbot++;
			} else {
				text = "GG. Player 2 won! Click Ok to exit.";
				winsplayer2++;
			}
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText(text);

		// reset all nececary options
		circle.setCenterX(500);
		circle.setCenterY(200);
		rec1.setLayoutY(150);
		rec2.setLayoutY(150);
		min = 3;
		max = 7;
		coutup = 0;
		top = false;
		bottom = true;
		right = true;
		left = false;
		space = true;
		r11 = false;
		r12 = false;
		r21 = false;
		r22 = false;
		x = 500;
		y = 200;
		player = 0;
		randomNumX = 3;
		randomNumY = 3;

		System.out.println(winsplayer1 + " " + winsplayer2 + " " + winsbot);

		label2 = new Label("Player one: " + winsplayer1 + "\nPlayer two: " + winsplayer2 + "\n\nBot: " + winsbot
				+ "\nPlayerBot: " + winsplayerbot);
		label2.setLayoutY(30);

		final Group group1 = new Group(label1, label2, button1, button2);
		scene1 = new Scene(group1, 200, 200);

		alert.setOnHidden(e -> stage.setScene(scene1));
		alert.show();
	}
}