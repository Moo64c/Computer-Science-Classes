import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ReversiGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int WINDOW_SIZE = 700;
	private JPanel panel;
	private JPanel board;
	private JLabel[][] boardLabels;
	private int player;
	private int size;
	private int[][] gameBoard;
	private JLabel statusLine;

	private ImageIcon player2Icon;
	private ImageIcon player1Icon;
	private ImageIcon possibleIcon;
	private JList<String> player2TypeList;
	private boolean allowPlay = true;

	private String player2Type;

	public ReversiGUI() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(LEFT_ALIGNMENT);

		final JPanel startChooser = new JPanel();
		startChooser.setLayout(new FlowLayout());

		final JTextField sizeText = new JTextField("8", 3);
		sizeText.setMaximumSize(new Dimension(30, 30));
		JButton runButton = new JButton("Go");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg) {
				String val = sizeText.getText();
				try {
					size = Integer.parseInt(val);
					if (size % 2 != 0 || size < 4 || size > 40) {
						System.err
								.println("Number must be in even in the range of 4 - 40");
					} else {
						gameBoard = ReversiPlay.createBoard(size);
						player = 1;
						statusLine.setText("Player 1 turn");
						if (gameBoard == null) {
							System.err
									.println("Board can't be created ! Got error from createBoard");
						} else {
							panel.remove(startChooser);
							panel.repaint();

							board.setLayout(new GridLayout(size, size, -1, -1));
							board.setBorder(BorderFactory.createEmptyBorder(2,
									2, 2, 2));

							boardLabels = new JLabel[size][size];
							player2Type = player2TypeList.getSelectedValue();
							// load images

							initIcons();

							for (int i = 0; i < size; i++) {
								for (int j = 0; j < size; j++) {
									boardLabels[i][j] = new JLabel("",
											SwingConstants.CENTER);
									final JLabel label = boardLabels[i][j];
									label.setBorder(BorderFactory
											.createLineBorder(Color.BLACK));
									label.setToolTipText("Row " + i
											+ " Column " + j);
									final int row = i;
									final int column = j;
									label.addMouseListener(new MouseListener() {

										@Override
										public void mouseReleased(
												MouseEvent arg0) {
											if (!allowPlay)
												return;
											System.out.println("User clicked "
													+ row + " " + column);
											try {
												// check that the user clicked
												// on a legal move for player
												if (ReversiPlay.isLegal(
														gameBoard, player, row,
														column)) {
													// play the move
													gameBoard = ReversiPlay
															.play(gameBoard,
																	player,
																	row, column);
													System.out
															.printf("Player %d added a tile to %d,%d\n",
																	player,
																	row, column);
													// change the player
													player = player == 1 ? 2
															: 1;

													if (!ReversiPlay.hasMoves(
															gameBoard, player)) {
														System.out
																.println("Player "
																		+ player
																		+ " has no moves, switching players");
														// back to player 1
														player = player == 1 ? 2
																: 1;
													} else {
														// try to play as
														// player 2 as long
														// as player 1 has
														// no moves
														while (player == 2
																&& !player2Type
																		.equals("Human")) {
															int[] move = null;
															if (player2Type
																	.equals("Random")) {
																move = ReversiPlay
																		.randomPlayer(
																				gameBoard,
																				player);
															} else if (player2Type
																	.equals("Greedy")) {
																move = ReversiPlay
																		.greedyPlayer(
																				gameBoard,
																				player);
															} else if (player2Type
																	.equals("Defensive")) {
																move = ReversiPlay
																		.defensivePlayer(
																				gameBoard,
																				player);
															} else if (player2Type
																	.equals("Location")) {
																move = ReversiPlay
																		.byLocationPlayer(
																				gameBoard,
																				player);
															} else if (player2Type
																	.equals("myPlayer")) {
																move = ReversiPlay
																		.myPlayer(
																				gameBoard,
																				player);
															}
															if (move == null) {
																// if has no
																// moves
																// then
																// change
																// the
																// current
																// player
																player = 1;
															} else if (move.length != 2
																	|| !ReversiPlay
																			.isLegal(
																					gameBoard,
																					player,
																					move[0],
																					move[1])) {
																System.err
																		.println("Player 2 didn't return a move or the move is not legal. check your code.");
																allowPlay = false;
															} else {
																// play the
																// move
																gameBoard = ReversiPlay
																		.play(gameBoard,
																				player,
																				move[0],
																				move[1]);
																System.out
																		.printf("Player %d added a tile to %d,%d\n",
																				player,
																				move[0],
																				move[1]);

																player = 1;
																if (!ReversiPlay
																		.hasMoves(
																				gameBoard,
																				player)) {
																	System.out
																			.println("Player "
																					+ player
																					+ " has no moves, switching players");
																	player = player == 1 ? 2
																			: 1;
																	;
																}
															}
														}
													}
													if (ReversiPlay
															.gameOver(gameBoard)) {
														System.out
																.printf("Game over. The winner is %d\n",
																		ReversiPlay
																				.findTheWinner(gameBoard));
														statusLine.setText("Game over. The winner is "
																+ ReversiPlay
																		.findTheWinner(gameBoard));
														allowPlay = false;

													} else {
														statusLine
																.setText("Player "
																		+ player
																		+ " turn");
													}
													updateBoardGUI();

												} else {
													System.err
															.printf("Move of player %d to %d,%d is not legal\n",
																	player,
																	row, column);
												}
											} catch (Throwable e) {
												System.out
														.println("Got error in play ...");
												e.printStackTrace();
												allowPlay = false;
											}

										}

										@Override
										public void mousePressed(MouseEvent arg0) {

										}

										@Override
										public void mouseExited(MouseEvent arg0) {

										}

										@Override
										public void mouseEntered(MouseEvent arg0) {

										}

										@Override
										public void mouseClicked(MouseEvent arg0) {

										}
									});
									board.add(label);
								}
							}

							updateBoardGUI();
							repaint();
							revalidate();
						}

					}

				} catch (NumberFormatException ex) {
					System.err.println("Must enter a number ... ");
				}

			}

			/**
			 * 
			 */
			public void initIcons() {
				ImageIcon icon = new ImageIcon("src/red1.gif");
				if (icon.getIconHeight() == -1){
					icon = new ImageIcon("Assignment2/red1.gif");
				}
				Image image = icon.getImage();
				Image newimg = image.getScaledInstance(WINDOW_SIZE / size - 5,
						WINDOW_SIZE / size - 5, java.awt.Image.SCALE_SMOOTH);
				player1Icon = new ImageIcon(newimg);

				ImageIcon icon2 = new ImageIcon("src/blue2.gif");
				if (icon2.getIconHeight() == -1){
					icon2 = new ImageIcon("Assignment2/blue2.gif");
				}
				Image image2 = icon2.getImage(); // transform it
				Image newimg2 = image2.getScaledInstance(
						WINDOW_SIZE / size - 5, WINDOW_SIZE / size - 5,
						java.awt.Image.SCALE_SMOOTH);
				player2Icon = new ImageIcon(newimg2);

				ImageIcon icon3 = new ImageIcon("src/possible.gif");
				if (icon3.getIconHeight() == -1){
					icon3 = new ImageIcon("Assignment2/possible.gif");
				}
				Image image3 = icon3.getImage();
				Image newimg3 = image3.getScaledInstance(WINDOW_SIZE
						/ (size * 2) - 5, WINDOW_SIZE / (size * 2) - 5,
						java.awt.Image.SCALE_SMOOTH);
				possibleIcon = new ImageIcon(newimg3);
			}

			/**
			 * @param boardLabels
			 */
			public void updateBoardGUI() {
				if (gameBoard == null) {
					System.err
							.println("Board is null ... please check your code");
					return;
				}
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						final JLabel label = boardLabels[i][j];

						label.setOpaque(true);
						if (gameBoard[i][j] == 1) {
							label.setIcon(player1Icon);
						} else if (gameBoard[i][j] == 2) {
							label.setIcon(player2Icon);
						} else {
							label.setIcon(null);
						}
						label.repaint();
					}
				}

				// try to update the possible moves
				int[][] moves = ReversiPlay.possibleMoves(gameBoard, player);
				if (moves != null) {
					for (int i = 0; i < moves.length; i++) {
						boardLabels[moves[i][0]][moves[i][1]]
								.setIcon(possibleIcon);
					}
				} else {
					System.err
							.println("Can't show possible moves - maybe not yet implemented :(");
				}
				board.repaint();
			}
		});

		startChooser.add(new JLabel("Size of board"));
		startChooser.add(sizeText);
		startChooser.add(runButton);
		player2TypeList = new JList<String>(new String[] { "Human", "Random",
				"Greedy", "Defensive", "Location", "myPlayer" });
		player2TypeList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		player2TypeList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		player2TypeList.setVisibleRowCount(0);
		player2TypeList.setSelectedIndex(0);
		startChooser.add(new JLabel("Player 2 type:"));
		startChooser.add(player2TypeList);
		board = new JPanel();

		panel.add(startChooser);
		panel.add(board);

		statusLine = new JLabel("Status line ...", SwingConstants.LEFT);
		statusLine.setSize(WINDOW_SIZE, 50);
		panel.add(statusLine);
		add(panel);
		setTitle("Reversi");
		setSize(WINDOW_SIZE, WINDOW_SIZE + 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ReversiGUI ex = new ReversiGUI();
				ex.setVisible(true);
			}
		});
	}
}
