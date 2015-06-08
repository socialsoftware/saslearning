package pt.ulisboa.tecnico.saslearning.signup;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.User;

@Controller
public class SignupController {

	@RequestMapping(value="/signup", method = RequestMethod.GET)
	public String signupForm(@SuppressWarnings("unused") UserForm user){
		return "signup";
	}
	
    @RequestMapping(value="/signup", method=RequestMethod.POST)
    public String checkUserInfo(@Valid UserForm user, BindingResult bindingResult, Model m) {
        boolean userAlreadyExists = checkIfUsernameExists(user.getUsername());
    	if (bindingResult.hasErrors() || userAlreadyExists) {
    		if(userAlreadyExists){
    			m.addAttribute("message", "Username " + user.getUsername() + " already exists.");
    		}
            return "signup";
        }
    	addNewUser(user);
        return "login";
    }

    @Atomic(mode=TxMode.WRITE)
    private void addNewUser(UserForm user) {
    	User newUser = new User();
    	newUser.setUsername(user.getUsername());
    	newUser.setPassword(user.getPassword());
    	newUser.setFirstName(user.getFirstName());
    	newUser.setLastName(user.getLastName());
    //TEMPORARY CODE TO ADD TEACHERS
    	if(user.getType() != null){
    		newUser.setType("TEACHER");
    	}else{
    		newUser.setType("STUDENT");
    	}
    //END OF TEMPORARY CODE	
    	FenixFramework.getDomainRoot().addUser(newUser);
	}

	@Atomic
    private boolean checkIfUsernameExists(String username){
    	Set<User> users = FenixFramework.getDomainRoot().getUserSet();
    	for(User u : users){
    		if(u.getUsername().equals(username)){
    			return true;
    		}
    	}
    	return false;
    }
}
