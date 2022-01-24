package com.ikhaa.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ikhaa.entities.Compte;
import com.ikhaa.entities.Operation;
import com.ikhaa.export.UserExcelExporter;
import com.ikhaa.metier.IBanqueMetier;

@Controller
public class BanqueController {

	@Autowired
	private IBanqueMetier banqueMetier;
	@RequestMapping("/operations")
	public String index() {
		return "comptes";
	}
	
	@RequestMapping("/consulterCompte")
	public String consulterCompte(Model model, String codeCompte,
			@RequestParam(name="page",defaultValue="0")int page,
			@RequestParam(name="size",defaultValue="5")int size) {
		model.addAttribute("codeCompte",codeCompte);
		try {
			Compte cp=banqueMetier.consulterCompte(codeCompte);
			Page<Operation> pageOperations=
					banqueMetier.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations",pageOperations.getContent());
			int[] pages= new int[pageOperations.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("compte",cp);
		}catch(Exception e) {
			model.addAttribute("exception",e);
		}
		
		return "comptes";
	}
	
	@RequestMapping(value="/saveOperation",method=RequestMethod.POST)
	public String saveOperation(Model model,String typeOperation,String codeCompte
			,double montant,String codeCompte2) {
		
		try{
			if(typeOperation.contentEquals("VERS")) {
				banqueMetier.verser(codeCompte, montant);
		}
		else if(typeOperation.equals("RETR")) {
			banqueMetier.retirer(codeCompte, montant);
		}
		} catch(Exception e) {
			model.addAttribute("error", e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte
					+"&error="+e.getMessage();

			
		}
		return "redirect:/consulterCompte?codeCompte="+codeCompte;
	}
	
	@RequestMapping("/users/export/excel")
        public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Operation> listUsers = banqueMetier.listAll();

		UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

		excelExporter.export(response);    
	    }  
}
