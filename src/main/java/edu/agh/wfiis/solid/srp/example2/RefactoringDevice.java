package edu.agh.wfiis.solid.srp.example2;

public class RefactoringDevice {

    public String provideDescription() {
        return "SRP description";
    }

    public String refactorCode(String badCode) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code refactored using SRP";
    }
}