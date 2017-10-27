package ch.pg.moving2;

import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class moving2Start2 extends Application {
	private static int KEYBOARD_MOVEMENT_DELTA = 5;
	private static final Duration TRANSLATE_DURATION = Duration.millis(1);

	private static boolean r11 = false, r12 = false, r21 = false, r22 = false, end = false, space = false;

	static int x = 500, y = 200, min = 3, max = 7, coutup = 0;

	private static boolean top = false, bottom = true, right = true, left = false;

	int randomNumX = 3, randomNumY = 3;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		final Circle circle = createCircle();
		final Rectangle rec1 = createRectangle1();
		final Rectangle rec2 = createRectangle2();
		final Group group = new Group(createInstructions(), circle, rec1, rec2);
		final TranslateTransition transition = createTranslateTransition(circle, rec1, rec2);

		final Scene scene = new Scene(group, 1000, 500, Color.CORNSILK);
		moveRecOnKeyPress(scene, rec1, rec2, circle, transition);
		moveRecOnKeyRelease(scene, rec1, rec2);

		stage.setScene(scene);
		stage.show();
	}

	private Label createInstructions() {
		Label instructions = new Label("Use the UP DOWN keys to move the right. Use the S W Keys to Move the left");
		instructions.setTextFill(Color.FORESTGREEN);
		return instructions;
	}

	private Circle createCircle() {
		final Circle circle = new Circle(500, 200, 20, Color.BLUEVIOLET);
		circle.setOpacity(0.7);
		return circle;
	}

	private Rectangle createRectangle1() {
		final Rectangle Rectangle1 = new Rectangle(20, 150, Color.BLUEVIOLET);
		Rectangle1.setOpacity(0.7);
		Rectangle1.setLayoutX(970);
		Rectangle1.setLayoutY(150);
		return Rectangle1;
	}

	private Rectangle createRectangle2() {
		final Rectangle Rectangle2 = new Rectangle(20, 150, Color.BLUEVIOLET);
		Rectangle2.setOpacity(0.7);
		Rectangle2.setLayoutX(10);
		Rectangle2.setLayoutY(150);
		return Rectangle2;
	}

	private TranslateTransition createTranslateTransition(final Circle circle, final Rectangle rec2,
			final Rectangle rec1) {
		final TranslateTransition transition = new TranslateTransition(TRANSLATE_DURATION, circle);

		if (end == true) {
			End();
			transition.stop();
		}

		transition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				Keymovent(rec1, rec2);
				if (circle.getCenterX() <= 5) {
					System.out.println("player 2 winns");
					createTranslateTransition(circle, rec1, rec2);
					end = true;
				} else if (circle.getCenterX() >= 1000) {
					System.out.println("player 1 winns");
					createTranslateTransition(circle, rec1, rec2);
					end = true;
				} else if (circle.getCenterY() >= 480 || circle.getCenterY() <= 20) {
					if (circle.getCenterY() >= 490) {
						// bottom
						Random r = new Random();
						randomNumY = r.nextInt(max - min) + min;
						top = false;
						bottom = true;
					} else if (circle.getCenterY() <= 20) {
						// Top
						Random r = new Random();
						randomNumY = r.nextInt(max - min) + min;
						top = true;
						bottom = false;
					}
				} else if (circle.getCenterY() <= (rec1.getLayoutY() + 150) && circle.getCenterY() >= rec1.getLayoutY()
						&& circle.getCenterX() <= 50) {
					// left Box
					Random r = new Random();
					randomNumX = r.nextInt(max - min) + min;
					left = false;
					right = true;
					
					coutup++;
				} else if (circle.getCenterY() <= (rec2.getLayoutY() + 150) && circle.getCenterY() >= rec2.getLayoutY()
						&& circle.getCenterX() >= 950) {
					// Right Box
					Random r = new Random();
					randomNumX = r.nextInt(max - min) + min;
					left = true;
					right = false;
					
					coutup++;
				}

				if (right == true) {
					x = x + randomNumX;
				} else if (left == true) {
					x = x - randomNumX;
				}

				if (top == true) {
					y = y + randomNumY;
				} else if (bottom == true) {
					y = y - randomNumY;
				}

				switch (coutup) {
				case 10:
					min = min + 1;
					max = max + 1;
					break;
				case 20:
					min = min + 1;
					max = max + 1;
					break;
				case 30:
					min = min + 2;
					max = max + 2;
					break;
				case 40:
					min = min + 2;
					max = max + 2;
					break;
				case 50:
					min = min + 3;
					max = max + 3;
					break;
				case 60:
					min = min + 3;
					max = max + 3;
					break;
				case 70:
					min = min + 4;
					max = max + 4;
					break;
				case 80:
					min = min + 4;
					max = max + 4;
					break;
				case 90:
					min = min + 5;
					max = max + 5;
					break;
				}

				circle.setCenterX(circle.getTranslateX() + circle.getCenterX());
				circle.setCenterY(circle.getTranslateY() + circle.getCenterY());
				circle.setTranslateX(0);
				circle.setTranslateY(0);

				moveCircleOnSpacePress(circle, transition, rec1, rec2);
			}
		});
		return transition;
	}

	private void moveRecOnKeyPress(Scene scene, final Rectangle rec2, final Rectangle rec1, final Circle circle,
			final TranslateTransition transition) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.SPACE || space == true) {
					moveCircleOnSpacePress(circle, transition, rec1, rec2);
					space = true;
				}

				if (event.getCode() == KeyCode.W || r11 == true) {
					if (rec1.getLayoutY() == 10) {

					} else {
						r11 = true;
					}
				}

				if (event.getCode() == KeyCode.S || r12 == true) {
					if (rec1.getLayoutY() == 340) {

					} else {
						r12 = true;
					}
				}

				if (event.getCode() == KeyCode.UP || r21 == true) {
					if (rec2.getLayoutY() == 10) {

					} else {
						r21 = true;
					}
				}

				if (event.getCode() == KeyCode.DOWN || r22 == true) {
					if (rec2.getLayoutY() == 340) {

					} else {
						r22 = true;
					}
				}
			}
		});
	}

	private void moveRecOnKeyRelease(Scene scene, final Rectangle rec2, final Rectangle rec1) {
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.W) {
					r11 = false;
				}

				if (event.getCode() == KeyCode.S) {
					r12 = false;
				}

				if (event.getCode() == KeyCode.UP) {
					r21 = false;
				}

				if (event.getCode() == KeyCode.DOWN) {
					r22 = false;
				}
			}
		});
	}

	private void moveCircleOnSpacePress(final Circle circle, final TranslateTransition transition, final Rectangle rec2,
			final Rectangle rec1) {
		transition.setToX(x - circle.getCenterX());
		transition.setToY(y - circle.getCenterY());

		transition.playFromStart();
	}

	@FXML
	public void End() {
		randomNumX = 0;
		randomNumY = 0;
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText("Good game. You won! Click OK to exit.");

		alert.setOnHidden(evt -> Platform.exit());

		alert.show();
		try {
			alert.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void Keymovent(final Rectangle rec1, final Rectangle rec2) {
		if (r11 == true) {
			if (rec1.getLayoutY() == 10) {

			} else {
				rec1.setLayoutY(rec1.getLayoutY() - KEYBOARD_MOVEMENT_DELTA);
				r11 = true;
			}
		}

		if (r12 == true) {
			if (rec1.getLayoutY() == 340) {

			} else {
				rec1.setLayoutY(rec1.getLayoutY() + KEYBOARD_MOVEMENT_DELTA);
				r12 = true;
			}
		}

		if (r21 == true) {
			if (rec2.getLayoutY() == 10) {

			} else {
				rec2.setLayoutY(rec2.getLayoutY() - KEYBOARD_MOVEMENT_DELTA);
				r21 = true;
			}
		}

		if (r22 == true) {
			if (rec2.getLayoutY() == 340) {

			} else {
				rec2.setLayoutY(rec2.getLayoutY() + KEYBOARD_MOVEMENT_DELTA);
				r22 = true;
			}
		}
	}
}