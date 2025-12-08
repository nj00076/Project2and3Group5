module edu.westga.cs3211.p1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens edu.westga.cs3211.p1.view to javafx.fxml;
    exports edu.westga.cs3211.p1;
}
