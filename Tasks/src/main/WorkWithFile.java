package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class WorkWithFile {
    private BufferedReader br;
    private Map<String, BufferedWriter> writers;
    private Map<String, BufferedReader> readers = new HashMap<String, BufferedReader>();
    private String path;

    public WorkWithFile(BufferedReader br, String outputDir,String sample) {
        this.br = br;
        this.path = outputDir + "\\" + sample;
        this.writers = new HashMap<>();
    }

    public boolean filter() {
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String type = getType(line);
				BufferedWriter writer = writers.get(type);
				if (writer == null) {
					writer = new BufferedWriter(new FileWriter(path + type + ".txt", true));
					writers.put(type, writer);
				}
				writer.write(line);
				writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения или записи  файла");
            System.exit(1);
        } finally {
			try {
				for (BufferedWriter writer : writers.values()) {
					writer.close();
				}
			} catch (IOException e) {
				System.err.println("Ошибка закрытия файла");
				System.exit(1);
			}
		}
        try {
        	for (BufferedWriter writer : writers.values()) {
                writer.close();
            }
		} catch (Exception e) {
			System.err.println("Ошибка закрытия файла");
			System.exit(1);
		}
        return true;
    }

    private String getType(String line) {
    	if(line.contains("E")) {
    		if (line.matches("-?\\d+(\\.\\d+)?E[-]?\\d+")) {
                return "floats";
            } else if(line.matches("-?\\d+(\\.\\d+)?E[+]?\\d+")) {
            	int count;
        		int indexPoint = line.indexOf('.');
        		BigInteger exponentValue = new BigInteger("0");
        		if (indexPoint == -1) {
        			count = 0;
        		} else {
        			int indexE = line.indexOf('E');
        	        count = indexE - indexPoint - 1;
        	        exponentValue = new BigInteger (line.substring(indexE + 1).replace("+", ""));
        		}
        		BigInteger countBigInt = BigInteger.valueOf(count);
                if(exponentValue.compareTo(countBigInt) < 0) {
                	return "floats";
                } else {
                	return "integers";
                }
            } else {
            	return "strings";
            }
    	}else {
    		if (line.matches("-?\\d+")) {
                return "integers";
            } else if (line.matches("-?\\d+(\\.\\d+)?([E][+-]?\\d+)?")) {
                return "floats";
            } else {
                return "strings";
            }
    	}
    }
    
    
    public void statGet(String option) {
    	String line;
		int maxInt = Integer.MIN_VALUE;
		int minInt = Integer.MAX_VALUE;
		String lineMaxInt="";
		String lineMinInt="";
		int sumInt = 0;
		double maxFloat = Double.MIN_VALUE;
		double minFloat = Double.MAX_VALUE;
		String lineMaxFloat="";
		String lineMinFloat="";
		double sumFloat = 0;
		int maxLength = 0;
		int minLength = Integer.MAX_VALUE;
		double avg;
		try {
    		for (String type : writers.keySet()) {
                BufferedReader reader = new BufferedReader(new FileReader(path + type + ".txt"));
                readers.put(type, reader);
            }
		} catch (FileNotFoundException e) {
			System.err.println("Ошибка чтения файла");
			System.exit(1);
		}
    	for(Map.Entry<String,BufferedReader> reader : readers.entrySet()) {
    		String type = reader.getKey();
    		BufferedReader br = reader.getValue();
    		int registredCount=0;
    		int notRegistredCount = 0;
    		try {
				while((line=br.readLine())!= null) {
					registredCount++; 
					switch (type) {
						case "integers": {
							try {
								if(line.contains("E")) {
									line = line.toLowerCase();
									if (Double.parseDouble(line) == Double.NEGATIVE_INFINITY || Double.parseDouble(line)==Double.POSITIVE_INFINITY || Double.parseDouble(line) == 0.0) {
										System.err.println("Ошибка чтения целого числа " + line + ", выход за границы вычисления (не будет учитываться  в расчетах)");
										notRegistredCount++;
										continue;
									}
									if (maxInt < Math.round(Double.parseDouble(line))) {
										maxInt = (int) Math.round(Double.parseDouble(line));
										lineMaxInt = line;
									}
									if (minInt > Math.round(Double.parseDouble(line))) {
										minInt = (int) Math.min(minInt, Math.round(Double.parseDouble(line)));
										lineMinInt = line;
									}
									sumInt += Math.round(Double.parseDouble(line));
								} else {
									if (maxInt < Integer.parseInt(line)) {
										maxInt = Integer.parseInt(line);
										lineMaxInt = line;
									}
									if (minInt > Integer.parseInt(line)) {
										minInt = Integer.parseInt(line);
										lineMinInt = line;
									}
									sumInt += Integer.parseInt(line);
								}
							} catch (NumberFormatException e) {
								System.err.println("Ошибка чтения целого числа " + line + ", выход за границы вычисления (не будет учитываться в расчетах)");
								notRegistredCount++;
								continue;
							}
							break;
						}
						case "floats": {
							try {
								if(line.contains("E")) {
									line = line.toLowerCase();
									if (Double.parseDouble(line) == Double.NEGATIVE_INFINITY || Double.parseDouble(line)==Double.POSITIVE_INFINITY || Double.parseDouble(line) == 0.0) {
										System.err.println("Ошибка чтения дробного числа " + line + "(либо 0), выход за границы вычисления (не будет учитываться в расчетах)");
										notRegistredCount++;
										continue;
									}
									if(maxFloat < Double.parseDouble(line)) {
										maxFloat = Double.parseDouble(line);
										lineMaxFloat = line;
									}
									if(minFloat > Double.parseDouble(line)) {
										minFloat = Double.parseDouble(line);
										lineMinFloat = line;
									}
									sumFloat += Double.parseDouble(line);
								} else {
									if (maxFloat <  Double.parseDouble(line)) {
										maxFloat = Double.parseDouble(line);
										lineMaxFloat = line;
									}
									if(minFloat > Double.parseDouble(line)) {
										minFloat = Double.parseDouble(line);
										lineMinFloat = line;
									}
									sumFloat+=Double.parseDouble(line);
								}
							} catch (NumberFormatException e) {
								System.err.println("Ошибка чтения дробного числа " + line + ", выход за границы вычисления (не будет учитываться  в расчетах)");
								notRegistredCount++;
								continue;
							}
							break;
						}
						case "strings": {
							maxLength = Math.max(maxLength,line.length());
							minLength = Math.min(minLength,line.length());
							break;
						}
					}
				}
				switch (type) {
					case "integers": {
						if(option.equals("s")) {
							System.out.println("Количество элементов типа " + type + ": " + registredCount);
						} else {
							avg = sumInt / (registredCount-notRegistredCount);
							
							System.out.println("Статистика по типу: " + type);
							System.out.println("Количество элементов: " + registredCount);
							System.out.println("Минимальное значение: " + lineMinInt);
							System.out.println("Максимальное значение: " + lineMaxInt);
							System.out.println("Сумма: " + sumInt);
							System.out.println("Среднее: " + avg);
						}
						break;
					}
					case "floats": {
						if(option.equals("s")) {
							System.out.println("Количество элементов типа " + type + ": " + registredCount);
						} else {
							avg = sumFloat / (registredCount-notRegistredCount);
							System.out.println("Статистика по типу: " + type);
							System.out.println("Количество элементов: " + registredCount);
							System.out.println("Минимальное значение: " + lineMinFloat);
							System.out.println("Максимальное значение: " + lineMaxFloat);
							System.out.println("Сумма: " + sumFloat);
							System.out.println("Среднее: " + avg);
						}
						break;
					}
					case "strings": {
						if(option.equals("s")) {
							System.out.println("Количество элементов типа " + type + ": " + registredCount);
						} else {
							System.out.println("Статистика по типу: " + type);
							System.out.println("Количество элементов: " + registredCount);
							System.out.println("Размер самой короткой строки: " + minLength);
							System.out.println("Размер самой длинной строки: " + maxLength);
						}
						break;
					}
				}
			} catch (IOException e) {
				System.err.println("Ошибка чтения файла");
				System.exit(1);
			}
    	}
    }
   
    
    
	
	
}

