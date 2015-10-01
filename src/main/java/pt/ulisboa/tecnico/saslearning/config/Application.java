package pt.ulisboa.tecnico.saslearning.config;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.User;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

@SpringBootApplication
@ComponentScan(basePackages = { "pt.ulisboa.tecnico.saslearning.*"})
public class Application implements InitializingBean {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Override
	@Atomic(mode=TxMode.WRITE)
	public void afterPropertiesSet() throws Exception {
		addNewUser("teacher", "1234567", "Catarina", "Santana", "TEACHER");
		FileInputStream fis = new FileInputStream("src/main/resources/ASof517_-_Alunos.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		HSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 0; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			HSSFCell usernameCell = row.getCell(0); //Provide Correct cell number
			String username = usernameCell.getStringCellValue();
			HSSFCell nameCell = row.getCell(0); //Provide Correct cell number
			String name = nameCell.getStringCellValue();
			String[] names = name.split(" ");
			String firstName = names[0];
			String lastName = names[name.length()-1];
			HSSFCell passwordCell = row.getCell(0); //Provide Correct cell number
			String password  = passwordCell.getStringCellValue();
			HSSFCell typeCell = row.getCell(0); //Provide Correct cell number
			String type  = passwordCell.getStringCellValue();
			//add users to DB here...
		}
		wb.close();
		
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addNewUser(String username, String password, String firstName, String lastName, String type) {
		if(!Utils.userExists("teacher")) {
			User u = new User();
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setUsername(username);
			u.setPassword(password);
			u.setType(type);
			FenixFramework.getDomainRoot().addUser(u);
		}
	}
}
