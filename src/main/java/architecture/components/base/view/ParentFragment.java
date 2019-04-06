package architecture.components.base.view;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;

import java.util.HashMap;

import static architecture.model.NamesPlugin.*;

/**
 * Created by alvaro on 15/07/2016.
 * Create fragment package and fragment class
 */
public class ParentFragment extends BaseViewController {
    private static PsiDirectory fragmentDirectory;

    public ParentFragment(Project project, PsiDirectory mainDirectory) {
        super(project, mainDirectory);
    }

    public static PsiDirectory getFragmentDirectory() {
        return fragmentDirectory;
    }

    public static void setFragmentDirectory(PsiDirectory fragmentDirectory) {
        ParentFragment.fragmentDirectory = fragmentDirectory;
    }

    public static void create() {

        // Check if exists fragment package
        PsiDirectory packageResult = containsPackage(getViewDirectory(), FRAGMENT.toLowerCase());

        // Not exists
        if (packageResult == null) {
            // Create fragment package
            fragmentDirectory = createDirectory(getViewDirectory(), FRAGMENT.toLowerCase());

            // Create ParentFragment.java
            HashMap<String, String> varTemplate = new HashMap<>();
            varTemplate.put("PACKAGE_PRESENTER", getPackageNameProject(Presenter.getPresenterDirectory()));
            varTemplate.put("PRESENTER", PRESENTER);

            Runnable runnable = () -> JavaDirectoryService.getInstance().createClass(fragmentDirectory, PARENT_FRAGMENT, BASE_FRAGMENT_TEMPLATE, false, varTemplate);
            WriteCommandAction.runWriteCommandAction(getProject(), runnable);
        } else {
            setFragmentDirectory(packageResult);

            if (fragmentDirectory.findFile(PARENT_FRAGMENT + ".java") == null) { // Not contains Constants.java
                // Create ParentFragment.java
                HashMap<String, String> varTemplate = new HashMap<>();
                varTemplate.put("PACKAGE_PRESENTER", getPackageNameProject(Presenter.getPresenterDirectory()));
                varTemplate.put("PRESENTER", PRESENTER);

                Runnable runnable = () -> JavaDirectoryService.getInstance().createClass(fragmentDirectory, PARENT_FRAGMENT, BASE_FRAGMENT_TEMPLATE, false, varTemplate);
                WriteCommandAction.runWriteCommandAction(getProject(), runnable);
            }
        }
    }
}
