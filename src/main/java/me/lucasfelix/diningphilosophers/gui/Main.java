package me.lucasfelix.diningphilosophers.gui;

import static me.lucasfelix.diningphilosophers.concurrence.PhilosopherState.THINKING;

import com.sun.glass.ui.PlatformAccessible;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.lucasfelix.diningphilosophers.concurrence.Philosopher;
import me.lucasfelix.diningphilosophers.concurrence.PhilosopherState;
import me.lucasfelix.diningphilosophers.concurrence.Semaphore;

public class Main extends Application {

	public static Semaphore mutex = new Semaphore(1);

	public static Semaphore[] semaphores = new Semaphore[5];

	public static ObservableList<PhilosopherState> states = FXCollections
			.observableArrayList();

	public static Philosopher philosophers[] = new Philosopher[5];

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			states.add(THINKING);
		}

		String[] names = { "MC Brinquedo", "Tiago Gates", "Platão", "Sócrates",
				"Aristóteles" };

		for (int i = 0; i < 5; i++) {
			philosophers[i] = new Philosopher(i, names[i], mutex);
		}

		for (int i = 0; i < 5; i++) {
			semaphores[i] = new Semaphore(0);
		}

		for (int i = 0; i < 5; i++) {
			philosophers[i].start();
		}

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle mainCircle = new Circle();
		mainCircle.setFill(Color.WHITE);
		mainCircle.setStroke(Color.BLACK);
		mainCircle.setStrokeWidth(5);
		mainCircle.setRadius(290.0f);

		PhilosopherBox philosopherBox0 = new PhilosopherBox(
				philosophers[0].getName(), states.get(0).getStateName());
		philosopherBox0.setLayoutY(40);
		philosopherBox0.setLayoutX(320);

		PhilosopherBox philosopherBox1 = new PhilosopherBox(
				philosophers[1].getName(), states.get(1).getStateName());
		philosopherBox1.setLayoutY(180);
		philosopherBox1.setLayoutX(170);

		PhilosopherBox philosopherBox2 = new PhilosopherBox(
				philosophers[2].getName(), states.get(2).getStateName());
		philosopherBox2.setLayoutY(380);
		philosopherBox2.setLayoutX(240);

		PhilosopherBox philosopherBox3 = new PhilosopherBox(
				philosophers[3].getName(), states.get(3).getStateName());
		philosopherBox3.setLayoutY(380);
		philosopherBox3.setLayoutX(440);

		PhilosopherBox philosopherBox4 = new PhilosopherBox(
				philosophers[4].getName(), states.get(4).getStateName());
		philosopherBox4.setLayoutY(180);
		philosopherBox4.setLayoutX(500);

		Group root = new Group();
		root.getChildren().addAll(mainCircle, philosopherBox0, philosopherBox1,
				philosopherBox2, philosopherBox3, philosopherBox4);

		Scene scene = new Scene(root, 800, 600);

		primaryStage.setTitle("Jantar dos filosofos");
		primaryStage.setScene(scene);
		primaryStage.show();

		mainCircle.setCenterX(primaryStage.getWidth() / 2);
		mainCircle.setCenterY(primaryStage.getHeight() / 2);

		states.addListener(new ListChangeListener<PhilosopherState>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends PhilosopherState> c) {
				Task<Void> task = new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						for (int i = 0; i < 5; i++) {

							int index = i;

							switch (index) {
							case 0:
								Platform.runLater(() -> {
									updateData(philosopherBox0,
											states.get(index));
								});
								break;
							case 1:
								Platform.runLater(() -> {
									updateData(philosopherBox1,
											states.get(index));
								});
								break;
							case 2:
								Platform.runLater(() -> {
									updateData(philosopherBox2,
											states.get(index));
								});
								break;
							case 3:
								Platform.runLater(() -> {
									updateData(philosopherBox3,
											states.get(index));
								});
								break;
							case 4:
								Platform.runLater(() -> {
									updateData(philosopherBox4,
											states.get(index));
								});
								break;
							}
						}
						return null;
					}

				};

				new Thread(task).start();

			}

			private void updateData(PhilosopherBox vbox, PhilosopherState state) {
				switch (state) {
				case HUNGRY:
					vbox.state.setText(state.getStateName());
					vbox.circlePhilosopher.setFill(Color.YELLOW);
					break;
				case THINKING:
					vbox.state.setText(state.getStateName());
					vbox.circlePhilosopher.setFill(Color.SILVER);
					break;
				case EATING:
					vbox.state.setText(state.getStateName());
					vbox.circlePhilosopher.setFill(Color.GREEN);
					break;
				}
			}

		});
	}

	class PhilosopherBox extends VBox {
		Circle circlePhilosopher = new Circle(50);
		Label name;
		Label state;

		public PhilosopherBox() {

		}

		public PhilosopherBox(String name, String state) {
			super();

			this.name = new Label(name);
			this.state = new Label(state);

			this.getChildren().addAll(circlePhilosopher, this.name, this.state);
			this.setAlignment(Pos.CENTER);

			this.name.setFont(new Font(20));
			this.state.setFont(new Font(20));

		}

	}

}
