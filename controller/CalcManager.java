package controller;

public class CalcManager {

    // Calculates IESS contribution
    static int calcIESS(float totalSalary){
        float iess = totalSalary * 0.0945f;
        return Math.round(iess);
    }

    // Calculates taxes
    static int calcImpRent(float totalSalary){
        float impRent = 0;
        float anualSalary = totalSalary * 12;

        if(anualSalary >= 0 && anualSalary <= 11902)
            impRent = 0;
        else if(anualSalary > 11902 && totalSalary <= 15159)
            impRent = (anualSalary - 11902) * 0.05f;
        else if(anualSalary > 15159 && totalSalary <= 19682)
            impRent = 163 + (anualSalary - 15159) * 0.1f;
        else if(anualSalary > 19682 && totalSalary <= 26031)
            impRent = 615 + (anualSalary - 19682) * 0.12f;
        else if(anualSalary > 26031 && totalSalary <= 34255)
            impRent = 1377 + (anualSalary - 26031) * 0.15f;
        else if(anualSalary > 34255 && totalSalary <= 45407)
            impRent = 2611 + (anualSalary - 34255) * 0.2f;
        else if(anualSalary > 45407 && totalSalary <= 60450)
            impRent = 4841 + (anualSalary - 45407) * 0.25f;
        else if(anualSalary > 60450 && totalSalary <= 80605)
            impRent = 8602 + (anualSalary - 60450) * 0.3f;
        else if(anualSalary > 80605 && totalSalary <= 107199)
            impRent = 14648 + (anualSalary - 80605) * 0.35f;
        else if(anualSalary > 107199)
            impRent = 23956 + (anualSalary - 107199) * 0.37f;

        impRent = impRent / 12; // Monthly tax
        return Math.round(impRent);
    }

    // Calculates net salary
    static int calcLiquidSalary(float totalSalary){
        return Math.round(totalSalary 
                          - calcIESS(totalSalary) - calcImpRent(totalSalary));
    }

    // Converts salary integer to words
    static String salaryToString(int salary){   
        String[] units = {"", "uno", "dos", "tres", "cuatro", "cinco", "seis",
                          "siete", "ocho", "nueve"};

        String[] teens = {"diez", "once", "doce", "trece", "catorce", "quince", 
                          "dieciséis", "diecisiete", "dieciocho", "diecinueve"};

        String[] tens = {"", "", "veinte", "treinta", "cuarenta", "cincuenta", 
                         "sesenta", "setenta", "ochenta", "noventa"};

        String[] hundreds = {"", "ciento", "doscientos", "trescientos", "cuatrocientos", 
                             "quinientos", "seiscientos", "setecientos", "ochocientos", 
                             "novecientos"};
    
        String salaryString = "";

        if(salary == 0) return "cero";
    
        // Thousands
        if(salary >= 1000){
            int thousandPart = salary / 1000;
            
            if (thousandPart == 1)
                salaryString += "mil ";            
            else 
                salaryString += salaryToString(thousandPart) + " mil ";
            
            salary %= 1000;
        }
    
        // Hundreds
        if(salary >= 100){
            int hundredPart = salary / 100;
            
            if(hundredPart == 1 && salary % 100 == 0) 
                salaryString += "cien ";
            else 
                salaryString += hundreds[hundredPart] + " ";
            salary %= 100;
        }
    
        // Tens and Units 
        if(salary >= 20){
            int tenPart = salary / 10;
            salaryString += tens[tenPart];
            
            if(salary % 10 != 0) 
                salaryString += " y " + units[salary % 10];
        } 
        else if(salary >= 10) 
            salaryString += teens[salary - 10];
        else if(salary > 0) 
            salaryString += units[salary];
            
        return salaryString.trim();
    }
}
