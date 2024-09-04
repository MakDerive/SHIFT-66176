package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
public class Main {
	//option -o
	public static File outputDir = new File("./");
	//option -a
	public static boolean reWrite=true;
	//option -s or -f
	public static String statOption = null;
	//option -p
	public static String sample = "";

	public static void main(String[] args) {
		setOptions(args);
		List<String> files = new ArrayList<String>();
		for(String arg : args) {
			if (arg.contains(".txt")) {
				files.add(arg);
			}
		}
		if (files.size() == 0) {
			System.out.println("Нет введенных файлов");
			System.exit(1);
		}
		filterFiles(files);
		
	}

	private static void setOptions(String[] args) {
		for(int i = 0;i < args.length;i++) {
			if(("-o").equals(args[i])) {
				outputDir = new File(args[i+1]);
			} else if ( ("-p").equals(args[i]) ) {
				sample = args[i+1];
			} else if( ("-a").equals(args[i]) ) {
				reWrite = false;
			} else if(("-s").equals(args[i]) ) {
				statOption = "s";
			} else if(("-f").equals(args[i])) {
				statOption = "f";
			}
		}
		
	}
	private static BufferedReader initializeReader(String file) {
	    try {
	        return new BufferedReader(new FileReader(file));
	    } catch (FileNotFoundException e) {
	        System.err.println("Файл " + file + " не найден. Пожалуйста, введите правильный путь, если хотите выйти напишите 0:");
	        while (true) {
	            try {
	                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	                String input = br.readLine();
	                if (input.equals("0")) {
	                    System.exit(1);
	                }
	                File filePath = new File(input);
	                if (!filePath.exists()) {
	                    System.out.println("Файл не существует, введите новый адрес или 0:");
	                } else {
	                    return new BufferedReader(new FileReader(filePath));
	                }
	            } catch (IOException e1) {
	                System.err.println("Ошибка чтения адреса");
	                System.exit(1);
	            }
	        }
	    }
	}
	
	private static void filterFiles(List<String> files) {
		WorkWithFile workWithFile = null;
		for(String file : files) {

				if (!outputDir.exists()) {
					System.out.println("Введите правильный путь, где сохранить результаты, если хотите выйти напишите 0: ");
				    while (true) {
				        try {
				            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				            String input = br.readLine();
				            if (input.equals("0")) {
				                System.exit(1);
				            }
				            outputDir = new File(input);
				            if (!outputDir.exists()) {
				                System.out.println("Директория не существует, введите новый адрес или 0:");
				            } else {
				                break;
				            }
				        } catch (IOException e) {
				            System.err.println("Ошибка чтения адреса повторите или введите 0");
				        }
				    }
				}
				
				if (reWrite) {
					List<String> types = new ArrayList<String>(Arrays.asList("integers.txt","floats.txt","strings.txt"));
					for(String type : types) {
						File filePath = new File(outputDir + "\\" + sample + type);
						if (filePath.exists()) {
							BufferedWriter bw;
							try {
								bw = new BufferedWriter(new FileWriter(filePath));
								bw.write("");
								bw.close();
							} catch (IOException e) {
								System.err.println("Ошибка записи файла");
					            System.exit(1);
							}
						}
					}
				}
				BufferedReader reader = initializeReader(file);
				workWithFile = new WorkWithFile(reader,outputDir.getPath(),sample);
				workWithFile.filter();
			
		}
		if (statOption != null) {
			workWithFile.statGet(statOption);
		}
	}
	

}
