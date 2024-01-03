package com.company;
import javafx.application.Application; import javafx.fxml.FXMLLoader; import javafx.scene.Parent;
import javafx.scene.Scene; import javafx.stage.Stage;
public class Main extends Application { public static final intHEIGHT = 100; public static final intWIDTH = 1280; @Override
public void start(Stage stage) throws Exception {
    FXMLLoaderfxmlLoader = new FXMLLoader(getClass().getResource("/Main.fxml")); Parent root = fxmlLoader.load();
    MainController controller = fxmlLoader.getController(); controller.setStage(stage);
    Scene scene = new Scene(root); stage.setTitle("Application"); stage.setScene(scene); stage.setHeight(HEIGHT + 40);
    stage.setWidth(WIDTH + 20); stage.show();}
    public static void main(String[] args) {
        launch(args);}}
//КласCell
public class Cell { public int row; public String name; public Cell L; public Cell R; public Cell U; public Cell D; public Header C;
    public Cell(Header header) { row = -1;
        L = this; R = this; U = this; D = this;
        C = header;}
    public void InsertLeft(Cell cell) { //додати новий зліва cell.L = L;
        L.R = cell; L = cell;
        cell.R = this;}
    public void InsertUp(Cell cell) { //додати новий зверху cell.U = U;
        U.D = cell; U = cell;

        cell.D = this;}}
//КласCoordinate
        import java.util.Objects; public class Coordinate {
    private final int x; private final int y;
    public Coordinate(int x, int y) { this.x = x;
        this.y = y;}
    public intgetX() { return x;}
    public intgetY() { return y;}
    @Override
    public boolean equals(Object o) { if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; Coordinate that = (Coordinate) o;
        return x == that.x&& y == that.y;}
    @Override
    public inthashCode() { return Objects.hash(x, y);}
    @Override
    public String toString() { return "Coordinate{" +
            "x=" + x + ", y=" + y + '}';}}
//КласDance
        import java.util.Stack;
        import java.util.function.Consumer; public class Dance {
    Header root;
    Header[] headers;
    Stack<Integer> answer; public Dance(int columns) {
        answer = new Stack<>(); headers = new Header[columns]; root = new Header(-1);
        for (int j = 0; j < columns; j++) { headers[j] = new Header(j);
            root.InsertLeft(headers[j]);}}
    public void AddRow(int row, int[] ones) { //додавання рядку Cell first = null;
        int last = -1;
        for (int x : ones) {
            Cell cell = new Cell(headers[x]); cell.row = row;
            headers[x].InsertUp(cell); headers[x].size++;

            if (x <= last) { //перерірканапорядокзростанняелементів
                throw new IllegalArgumentException("Column indexes must be in increment order");} last = x;
            if (first == null) first = cell; else first.InsertLeft(cell);}}
    public void dance(int step, Consumer<Stack<Integer>>answerConsumer) { Header head = (Header) root.R;
        while (head.size == 0 && head != root) { head = (Header) head.R;}
        if (root.R == root) { System.out.println("Found Answer");
            for (int row : answer) { System.out.print(row + " ");} System.out.println(); answerConsumer.accept(answer);
            return;}
//вибірнайменшогостопця intminSize = head.size;
        for (Cell jCell = head; jCell != root; jCell = jCell.R) { if (((Header) jCell).size <minSize){
            minSize = ((Header)jCell).size;
            head = (Header)jCell;} } cover(head);
        for (Cell rCell = head.D; rCell != head; rCell = rCell.D) {
//save row number answer.push(rCell.row);
            for (Cell jCell = rCell.R; jCell != rCell; jCell = jCell.R) { cover(jCell.C);}
            dance(step + 1, answerConsumer); answer.pop();
            for (Cell jCell = rCell.L; jCell != rCell; jCell = jCell.L) { uncover(jCell.C);}}
        uncover(head);}
    private void cover(Header head) {//відкриттястовпця head.R.L = head.L;
        head.L.R = head.R;
        for (Cell iCell = head.D; iCell != head; iCell = iCell.D) { for (Cell jCell = iCell.R; iCell != jCell; jCell = jCell.R) {
            jCell.D.U = jCell.U; jCell.U.D = jCell.D; jCell.C.size--;}}}
    private void uncover(Header head) {//закриттястовпця
        for (Cell iCell = head.U; iCell != head; iCell = iCell.U) { for (Cell jCell = iCell.L; iCell != jCell; jCell = jCell.L) {
            jCell.D.U = jCell; jCell.U.D = jCell; jCell.C.size++;}} head.R.L = head; head.L.R = head;}}

//КласFigure

public class Figure { public int total;
    public Variant[] variants;}
//КласHeader
public class Header extends Cell { public int name;
    public int size;
    public Header(int name) { super(null);
        this.name = name; size = 0;
        C = this;}} КласMainController import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.layout.AnchorPane; import javafx.scene.paint.Color;
        import javafx.scene.shape.Rectangle; import javafx.stage.Stage;
        import java.net.URL; import java.util.*
public class MainController implements Initializable { @FXML
private AnchorPaneanchorPane; private Stage stage;
    private Map<Coordinate, Rectangle>cellsView = new HashMap<>(); private static final intWIDTH = 20;
    private static final intHEIGHT = 3;
    private static final List<Color>COLORS = new ArrayList<>(); static {
        COLORS.add(Color.BLACK);
        COLORS.add(Color.BLUE); COLORS.add(Color.GREEN); COLORS.add(Color.YELLOW);
        COLORS.add(Color.RED); COLORS.add(Color.PURPLE); COLORS.add(Color.CHOCOLATE); COLORS.add(Color.ORANGE); COLORS.add(Color.SKYBLUE); COLORS.add(Color.OLIVE); COLORS.add(Color.PALETURQUOISE); COLORS.add(Color.AZURE);}
    @Override
    public void initialize(URL location, ResourceBundle resources) { intrecWidth = Main.WIDTH/ WIDTH;
        intrecHeight = Main.HEIGHT/ HEIGHT; anchorPane.setMinWidth(Main.WIDTH); anchorPane.setMinHeight(Main.HEIGHT); anchorPane.setMaxWidth(Main.WIDTH); anchorPane.setMaxHeight(Main.HEIGHT);
        for (int j = 0; j <HEIGHT; j++) {

            for (inti = 0; i<WIDTH; i++) {
                Rectangle rectangle = new Rectangle(i * recWidth, j * recHeight, recWidth, recHeight);
                rectangle.setFill(Color.WHITE); rectangle.setStroke(Color.BLACK); anchorPane.getChildren().add(rectangle);
                cellsView.put(new Coordinate(i, HEIGHT - 1 - j), rectangle);}} startPent(WIDTH, HEIGHT);}
    void startPent(int a, int b) {
        Dance dance = new Dance(12 + 60); //12 наномерфігури Pentaminospentaminos = new Pentaminos();
//pentaminos.show(1, pentaminos.figures[5].variants[1], 0, 0); List<PreView>preViews = new ArrayList<>();
        int nr = 0; //номеррядку intfn = 0; //номерфігури
        for (Figure figure : pentaminos.figures) { intvn = 0; // варіантфігури
            for (Variant variant : figure.variants) {
                for (intsx = 0; sx< a; sx++) {//зміщенняпох for (intsy = 0; sy< b; sy++) {
                    boolean can = true;
                    for (int j = 0; j <variant.x.length&& can; j++) {
                        if (variant.x[j] + sx< 0 || variant.x[j] + sx>= a) { can = false;}
                        if (variant.y[j] + sy< 0 || variant.y[j] + sy>= b) { can = false;}}
                    if (!can) continue;
//формуванняматриці dance.AddRow(nr, new int[]{
                    fn,
                            12 + variant.x[0] + sx + a * (variant.y[0] + sy), 12 + variant.x[1] + sx + a * (variant.y[1] + sy), 12 + variant.x[2] + sx + a * (variant.y[2] + sy), 12 + variant.x[3] + sx + a * (variant.y[3] + sy), 12 + variant.x[4] + sx + a * (variant.y[4] + sy)});
                System.out.println(nr + " " + fn + " " + vn + " " + sx + " " + sy); preViews.add(new PreView(nr, fn, vn, sx, sy));
                nr++;}}
        vn++;}
    fn++;}
dance.dance(0, answer -> {
        for (Integer row : answer) { PreViewpreView = preViews.stream()
        .filter(p ->p.getNr() == row)
        .findAny()
        .get(); intnumOfFigure = preView.getFn();
        Figure figure = pentaminos.figures[numOfFigure]; Variant variant = figure.variants[preView.getVn()];
        intoffsetX = preView.getSx(); intoffsetY = preView.getSy();

        int size = variant.x.length; intstartX = variant.x[0] + offsetX; intstartY = variant.y[0] + offsetY;
        cellsView.get(new Coordinate(startX, startY)).setFill(COLORS.get(numOfFigure)); for (inti = 1; i< size; i++) {
        int x = startX + variant.x[i]; int y = startY + variant.y[i];
        cellsView.get(new Coordinate(x, y)).setFill(COLORS.get(numOfFigure));}}});} public void setStage(Stage stage) {
        this.stage = stage;}}
        КласPentaminos
public class Pentaminos { public Figure[] figures;
    public String names = "FILNPUVWXYZ"; public Pentaminos() {
        addFigure();
    }
    void addFigure() {
        figures = new Figure[12]; figures[0] = new Figure();
        figures[0].total = 2;	// F figures[0].variants = new Variant[figures[0].total]; figures[0].variants[0] = new Variant(); figures[0].variants[1] = new Variant();
        figures[0].variants[0].x = new int[] { 0, 1, -1, 0, 0 }; figures[0].variants[0].y = new int[] { 0,
                0, 1, 1, 2 };
        figures[0].variants[1].x = new int[] { 0, -1, 0, 1, 1 }; figures[0].variants[1].y = new int[] { 0,
                1, 1, 1, 2 };
        figures[1] = new Figure();
        figures[1].total = 2;	// I figures[1].variants = new Variant[figures[1].total]; figures[1].variants[0] = new Variant(); figures[1].variants[1] = new Variant();
        figures[1].variants[0].x = new int[] { 0, 0, 0, 0, 0 }; figures[1].variants[0].y = new int[] { 0,
                1, 2, 3, 4 };
        figures[1].variants[1].x = new int[] { 0, 1, 2, 3, 4 }; figures[1].variants[1].y = new int[] { 0,
                0, 0, 0, 0 };
        figures[2] = new Figure();
        figures[2].total = 8;	// L figures[2].variants = new Variant[figures[2].total]; figures[2].variants[0] = new Variant(); figures[2].variants[1] = new Variant(); figures[2].variants[2] = new Variant(); figures[2].variants[3] = new Variant(); figures[2].variants[4] = new Variant(); figures[2].variants[5] = new Variant(); figures[2].variants[6] = new Variant(); figures[2].variants[7] = new Variant();
        figures[2].variants[0].x = new int[] { 0, 0, 0, 0, 1 }; figures[2].variants[0].y = new int[] { 0,
                1, 2, 3, 3 };
        figures[2].variants[1].x = new int[] { 0, 1, 2, 3, 0 }; figures[2].variants[1].y = new int[] { 0,

                0, 0, 0, 1 };
        figures[2].variants[2].x = new int[] { 0, 1, 1, 1, 1 }; figures[2].variants[2].y = new int[] { 0,
                0, 1, 2, 3 };
        figures[2].variants[3].x = new int[] { 0, -3, -2, -1, 0 }; figures[2].variants[3].y = new int[] {
                0, 1, 1, 1, 1 };
        figures[2].variants[4].x = new int[] { 0, 0, 0, -1, 0 }; figures[2].variants[4].y = new int[] { 0,
                1, 2, 3, 3 };
        figures[2].variants[5].x = new int[] { 0, 1, 2, 3, 3 }; figures[2].variants[5].y = new int[] { 0,
                0, 0, 0, 1 };
        figures[2].variants[6].x = new int[] { 0, 1, 0, 0, 0 }; figures[2].variants[6].y = new int[] { 0,
                0, 1, 2, 3 };
        figures[2].variants[7].x = new int[] { 0, 0, 1, 2, 3 }; figures[2].variants[7].y = new int[] { 0,
                1, 1, 1, 1 };
        figures[3] = new Figure();
        figures[3].total = 8;	// N figures[3].variants = new Variant[figures[3].total]; figures[3].variants[0] = new Variant(); figures[3].variants[1] = new Variant(); figures[3].variants[2] = new Variant(); figures[3].variants[3] = new Variant(); figures[3].variants[4] = new Variant(); figures[3].variants[5] = new Variant(); figures[3].variants[6] = new Variant(); figures[3].variants[7] = new Variant();
        figures[3].variants[0].x = new int[] { 0, 1, 1, 2, 3 }; figures[3].variants[0].y = new int[] { 0,
                0, 1, 1, 1 };
        figures[3].variants[1].x = new int[] { 0, -1, 0, -1, -1 }; figures[3].variants[1].y = new int[] {
                0, 1, 1, 2, 3 };
        figures[3].variants[2].x = new int[] { 0, 1, 2, 2, 3 }; figures[3].variants[2].y = new int[] { 0,
                0, 0, 1, 1 };
        figures[3].variants[3].x = new int[] { 0, 0, -1, 0, -1 }; figures[3].variants[3].y = new int[] {
                0, 1, 2, 2, 3 };
        figures[3].variants[4].x = new int[] { 0, 1, -2, -1, 0 }; figures[3].variants[4].y = new int[] {
                0, 0, 1, 1, 1 };
        figures[3].variants[5].x = new int[] { 0, 0, 1, 1, 1 }; figures[3].variants[5].y = new int[] { 0,
                1, 1, 2, 3 };
        figures[3].variants[6].x = new int[] { 0, 1, 2, -1, 0 }; figures[3].variants[6].y = new int[] { 0,
                0, 0, 1, 1 };
        figures[3].variants[7].x = new int[] { 0, 0, 0, 1, 1 }; figures[3].variants[7].y = new int[] { 0,
                1, 2, 2, 3 };
        figures[4] = new Figure();
        figures[4].total = 8;	// P figures[4].variants = new Variant[figures[4].total]; figures[4].variants[0] = new Variant(); figures[4].variants[1] = new Variant(); figures[4].variants[2] = new Variant(); figures[4].variants[3] = new Variant(); figures[4].variants[4] = new Variant(); figures[4].variants[5] = new Variant(); figures[4].variants[6] = new Variant(); figures[4].variants[7] = new Variant();

        figures[4].variants[0].x = new int[] { 0, 1, 0, 1, 0 }; figures[4].variants[0].y = new int[] { 0,
                0, 1, 1, 2 };
        figures[4].variants[1].x = new int[] { 0, 1, 2, 1, 2 }; figures[4].variants[1].y = new int[] { 0,
                0, 0, 1, 1 };
        figures[4].variants[2].x = new int[] { 0, -1, 0, -1, 0 }; figures[4].variants[2].y = new int[] {
                0, 1, 1, 2, 2 };
        figures[4].variants[3].x = new int[] { 0, 1, 0, 1, 2 }; figures[4].variants[3].y = new int[] { 0,
                0, 1, 1, 1 };
        figures[4].variants[4].x = new int[] { 0, 1, 0, 1, 1 }; figures[4].variants[4].y = new int[] { 0,
                0, 1, 1, 2 };
        figures[4].variants[5].x = new int[] { 0, 1, 2, 0, 1 }; figures[4].variants[5].y = new int[] { 0,
                0, 0, 1, 1 };
        figures[4].variants[6].x = new int[] { 0, 0, 1, 0, 1 }; figures[4].variants[6].y = new int[] { 0,
                1, 1, 2, 2 };
        figures[4].variants[7].x = new int[] { 0, 1, -1, 0, 1 }; figures[4].variants[7].y = new int[] { 0,
                0, 1, 1, 1 };
        figures[5] = new Figure();
        figures[5].total = 4;	// T figures[5].variants = new Variant[figures[5].total]; figures[5].variants[0] = new Variant(); figures[5].variants[1] = new Variant(); figures[5].variants[2] = new Variant(); figures[5].variants[3] = new Variant();
        figures[5].variants[0].x = new int[] { 0, 1, 2, 1, 1 }; figures[5].variants[0].y = new int[] { 0,
                0, 0, 1, 2 };
        figures[5].variants[1].x = new int[] { 0, -2, -1, 0, 0 }; figures[5].variants[1].y = new int[] {
                0, 1, 1, 1, 2 };
        figures[5].variants[2].x = new int[] { 0, 0, -1, 0, 1 }; figures[5].variants[2].y = new int[] { 0,
                1, 2, 2, 2 };
        figures[5].variants[3].x = new int[] { 0, 0, 1, 2, 0 }; figures[5].variants[3].y = new int[] { 0,
                1, 1, 1, 2 };
        figures[6] = new Figure();
        figures[6].total = 4;	// U figures[6].variants = new Variant[figures[6].total]; figures[6].variants[0] = new Variant(); figures[6].variants[1] = new Variant(); figures[6].variants[2] = new Variant(); figures[6].variants[3] = new Variant();
        figures[6].variants[0].x = new int[] { 0, 1, 1, 0, 1 }; figures[6].variants[0].y = new int[] { 0,
                0, 1, 2, 2 };
        figures[6].variants[1].x = new int[] { 0, 1, 2, 0, 2 }; figures[6].variants[1].y = new int[] { 0,
                0, 0, 1, 1 };
        figures[6].variants[2].x = new int[] { 0, 1, 0, 0, 1 }; figures[6].variants[2].y = new int[] { 0,
                0, 1, 2, 2 };
        figures[6].variants[3].x = new int[] { 0, 2, 0, 1, 2 }; figures[6].variants[3].y = new int[] { 0,
                0, 1, 1, 1 };
        figures[7] = new Figure();
        figures[7].total = 4;	// V figures[7].variants = new Variant[figures[7].total]; figures[7].variants[0] = new Variant(); figures[7].variants[1] = new Variant();

        figures[7].variants[2] = new Variant(); figures[7].variants[3] = new Variant();
        figures[7].variants[0].x = new int[] { 0, 0, 0, 1, 2 }; figures[7].variants[0].y = new int[] { 0,
                1, 2, 2, 2 };
        figures[7].variants[1].x = new int[] { 0, 1, 2, 0, 0 }; figures[7].variants[1].y = new int[] { 0,
                0, 0, 1, 2 };
        figures[7].variants[2].x = new int[] { 0, 1, 2, 2, 2 }; figures[7].variants[2].y = new int[] { 0,
                0, 0, 1, 2 };
        figures[7].variants[3].x = new int[] { 0, 0, -2, -1, 0 }; figures[7].variants[3].y = new int[] {
                0, 1, 2, 2, 2 };
        figures[8] = new Figure();
        figures[8].total = 4;	// W figures[8].variants = new Variant[figures[8].total]; figures[8].variants[0] = new Variant(); figures[8].variants[1] = new Variant(); figures[8].variants[2] = new Variant(); figures[8].variants[3] = new Variant();
        figures[8].variants[0].x = new int[] { 0, 0, 1, 1, 2 }; figures[8].variants[0].y = new int[] { 0,
                1, 1, 2, 2 };
        figures[8].variants[1].x = new int[] { 0, 1, -1, 0, -1 }; figures[8].variants[1].y = new int[] {
                0, 0, 1, 1, 2 };
        figures[8].variants[2].x = new int[] { 0, 1, 1, 2, 2 }; figures[8].variants[2].y = new int[] { 0,
                0, 1, 1, 2 };
        figures[8].variants[3].x = new int[] { 0, -1, 0, -2, -1 }; figures[8].variants[3].y = new int[] {
                0, 1, 1, 2, 2 };
        figures[9] = new Figure();
        figures[9].total = 1;	// X figures[9].variants = new Variant[figures[9].total]; figures[9].variants[0] = new Variant();
        figures[9].variants[0].x = new int[] { 0, -1, 0, 1, 0 }; figures[9].variants[0].y = new int[] { 0,
                1, 1, 1, 2 };
        figures[10] = new Figure();
        figures[10].total = 8;	// Y figures[10].variants = new Variant[figures[10].total]; figures[10].variants[0] = new Variant(); figures[10].variants[1] = new Variant(); figures[10].variants[2] = new Variant(); figures[10].variants[3] = new Variant(); figures[10].variants[4] = new Variant(); figures[10].variants[5] = new Variant(); figures[10].variants[6] = new Variant(); figures[10].variants[7] = new Variant();
        figures[10].variants[0].x = new int[] { 0, -1, 0, 0, 0 }; figures[10].variants[0].y = new int[] {
                0, 1, 1, 2, 3 };
        figures[10].variants[1].x = new int[] { 0, -2, -1, 0, 1 }; figures[10].variants[1].y = new int[]
                { 0, 1, 1, 1, 1 };
        figures[10].variants[2].x = new int[] { 0, 0, 0, 1, 0 }; figures[10].variants[2].y = new int[] {
                0, 1, 2, 2, 3 };
        figures[10].variants[3].x = new int[] { 0, 1, 2, 3, 1 }; figures[10].variants[3].y = new int[] {
                0, 0, 0, 0, 1 };
        figures[10].variants[4].x = new int[] { 0, 0, 1, 0, 0 }; figures[10].variants[4].y = new int[] {

                0, 1, 1, 2, 3 };
        figures[10].variants[5].x = new int[] { 0, -1, 0, 1, 2 }; figures[10].variants[5].y = new int[] {
                0, 1, 1, 1, 1 };
        figures[10].variants[6].x = new int[] { 0, 0, -1, 0, 0 }; figures[10].variants[6].y = new int[] {
                0, 1, 2, 2, 3 };
        figures[10].variants[7].x = new int[] { 0, 1, 2, 3, 2 }; figures[10].variants[7].y = new int[] {
                0, 0, 0, 0, 1 };
        figures[11] = new Figure();
        figures[11].total = 4;	// Z figures[11].variants = new Variant[figures[11].total]; figures[11].variants[0] = new Variant(); figures[11].variants[1] = new Variant(); figures[11].variants[2] = new Variant(); figures[11].variants[3] = new Variant();
        figures[11].variants[0].x = new int[] { 0, 1, 1, 1, 2 };	figures[11].variants[0].y = new int[]
                { 0, 0, 1, 2, 2 };
        figures[11].variants[1].x = new int[] { 0, -2, -1, 0, -2 }; figures[11].variants[1].y = new int[]
                { 0, 1, 1, 1, 2 };
        figures[11].variants[2].x = new int[] { 0, 1, 0, -1, 0 };  figures[11].variants[2].y = new int[]
                { 0, 0, 1, 2, 2 };
        figures[11].variants[3].x = new int[] { 0, 0, 1, 2, 2 };	figures[11].variants[3].y = new int[]
                { 0, 1, 1, 1, 2 }}}
//КласPreView
public class PreView { private int nr; private intfn; private intvn; private intsx; private intsy;
    public PreView(int nr, intfn, intvn, intsx, intsy) { this.nr = nr;
        this.fn = fn;
        this.vn = vn; this.sx = sx;
        this.sy = sy;} public intgetNr() {
        return nr;} public intgetFn() {
        return fn;} public intgetVn() {
        return vn;} public intgetSx() {
        return sx;} public intgetSy() {
        return sy;}}
//КласVariant
public class Variant { public int[] x; public int[] y;}
