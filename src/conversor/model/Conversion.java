package conversor.model;

public class Conversion {
	
	private String signo_IEEE;
	private String exponente_IEEE;
	private String mantisa_IEEE;
	private int exponente_decimal;
	private double numero_decimal;
	
	public Conversion() {}

    public Conversion(String signo, String exponente, String mantisa, int exp, double num) {
        this.signo_IEEE = signo;
        this.exponente_IEEE = exponente;
        this.mantisa_IEEE = mantisa;
        this.exponente_decimal = exp;
        this.numero_decimal = num;
    }

    public String getSigno() {
        return signo_IEEE;
    }

    public void setSigno(String signo) {
        this.signo_IEEE = signo;
    }

    public String getExponente_IEEE() {
        return exponente_IEEE;
    }

    public void setExponente_IEEE(String exponente) {
        this.exponente_IEEE = exponente;
    }

    public String getMantisa() {
        return mantisa_IEEE;
    }

    public void setMantisa(String mantisa) {
        this.mantisa_IEEE = mantisa;
    }
    
    public int getExponente_decimal() {
        return exponente_decimal;
    }

    public void setExponente_decimal(int exponente) {
        this.exponente_decimal = exponente;
    }
    
    public double getNumero_decimal() {
        return numero_decimal;
    }

    public void setNumero_decimal(double numero) {
        this.numero_decimal = numero;
    }
	
	public String convIEEE754(double num, String precision) {
		
		int bias, exp_length, mant_length;
    	
		if (precision.equals("Sencilla")) {
    	    bias = 127;
    		exp_length = 8;
    		mant_length = 23;
    	} else {
    		bias = 1023;
    		exp_length = 11;
    		mant_length = 52;
    	}
    	
    	if (num == 0.0) {
    		setSigno("0");
    		setExponente_IEEE("0".repeat(exp_length));
    		setMantisa("0".repeat(mant_length));
            return convBinToHex(this.signo_IEEE + this.exponente_IEEE + this.mantisa_IEEE);
        }
    	
    	int signo = (num < 0) ? 1 : 0;
    	setSigno(String.valueOf(signo));
    	
        num = Math.abs(num);
    	
    	//1 Paso: Normalización
    	int pot = 0;
    	if(num < 1) {
    		while(num < 1) {
    			num *= 2;
    			pot--;
    		}
    	}
    	else {
    		while(num >= 2) {
        		num /= 2;
        		pot++;
        	}
    	}
    	
    	//2 Paso: Exponente
    	pot += bias;
    	String exponente = convDecToBin(pot);
    	while(exponente.length() < exp_length) {
    		exponente = "0" + exponente;
    	}
    	setExponente_IEEE(exponente);
    	
    	//3 Paso: Mantisa
    	double parteDecimal = num-1;
    	String mantisa = "";
    	
    	while(mantisa.length() < mant_length && parteDecimal > 0) {
    		
    		parteDecimal *= 2;
    		int bit = (int) parteDecimal;
    		mantisa += bit;
    		parteDecimal -= bit;
    	}
    	
    	while(mantisa.length() < mant_length) {
    		mantisa += "0";
    	}
    	
    	setMantisa(mantisa);
    	
    	//4 Paso: Representar
    	String result = signo + exponente + mantisa;
    	
    	return convBinToHex(result);
    }
    
    public double convDecimal(String formatoIEEE) throws Exception {
    	
    	double result = 0.0;
    	int exponente = 0;
    	double mantisa = 1.0;
    	
    	int exp_length = 0;
    	int bias = 0;
    	int mant_length = 0;
    	
    	if(formatoIEEE.length() == 32) {
    	    exp_length = 8;
    	    bias = 127;
    	    mant_length = 23;
    	} else {
    		if(formatoIEEE.length() == 64) {
    			exp_length = 11;
    			bias = 1023;
    			mant_length = 52;
    		}
    		else {
    			throw new Exception("Asegúrese de que el número tenga el tamaño correspondiente a su precisión (32 o 64 bits)");
    		}
    	}
    	
    	int signo = formatoIEEE.charAt(0) - '0';
        @SuppressWarnings("unused")
		String expBits = formatoIEEE.substring(1, 1 + exp_length);
        String mantBits = formatoIEEE.substring(1 + exp_length);
    	
        int pot = exp_length-1;
    	
    	for(int i = 1; i <= exp_length; i++) {
    		if(formatoIEEE.charAt(i) == '1') {
    			exponente += Math.pow(2, pot);
    		}
    		pot--;
    	}
    	
    	if (exponente == 0 && mantBits.replace("0", "").isEmpty()) {
            return signo == 1 ? -0.0 : 0.0;
        }
        if (exponente == (Math.pow(2, exp_length) - 1)) {
            if (mantBits.replace("0", "").isEmpty()) {
                return signo == 1 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            } else {
                return Double.NaN;
            }
        }
        
        pot = -1;
    	for(int i = formatoIEEE.length() - mant_length; i < formatoIEEE.length(); i++) {
    		if(formatoIEEE.charAt(i) == '1') {
    			mantisa += Math.pow(2, pot);
    		}
    		pot--;
    	}
    	
    	exponente -= bias;
    	setExponente_decimal(exponente);
    	
    	result = Math.pow(-1, signo) * mantisa;
    	setNumero_decimal(result);
    	result *= Math.pow(2, exponente);
    	
    	return result;
    }
    
    public String convDecToBin(int decimal) {
		
		if (decimal == 0) {
			return "0";
		}
		
		StringBuilder respuesta = new StringBuilder();
		while (decimal != 0) {
		    int residuo = decimal % 2;
		    respuesta.append(residuo);
		    decimal /= 2;
		}
		return respuesta.reverse().toString();
	}
    
    public String convBinToHex(String binario){
		
		String hexadecimal = "";

	    while (binario.length() % 4 != 0) {
	        binario = "0" + binario;
	    }

	    for (int i = binario.length() - 1; i >= 0; i -= 4) {
	        int valor1 = binario.charAt(i) - '0';
	        int valor2 = binario.charAt(i - 1) - '0';
	        int valor3 = binario.charAt(i - 2) - '0';
	        int valor4 = binario.charAt(i - 3) - '0';

	        int digito = valor1 + valor2 * 2 + valor3 * 4 + valor4 * 8;
	        
	        if(digito < 10) {
	        	hexadecimal += digito;
			}
			else {
				if(digito == 10) hexadecimal += "A";
				if(digito == 11) hexadecimal += "B";
				if(digito == 12) hexadecimal += "C";
				if(digito == 13) hexadecimal += "D";
				if(digito == 14) hexadecimal += "E";
				if(digito == 15) hexadecimal += "F";
			}

	    }

	    return new StringBuilder(hexadecimal).reverse().toString();
	}

}
