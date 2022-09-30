package mx.caedex.labs.main;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainApplication {
    public static void main(String [] args) {
        System.out.println("Filtro de frecuencias");
        List<List<Integer>> rangeSignals = new ArrayList<>();

        //Dupla 1
        List<Integer> d1 = new ArrayList<>();
        d1.add(2);
        d1.add(8);

        rangeSignals.add(d1);

        //Dupla 2
        List<Integer> d2 = new ArrayList<>();
        d2.add(4);
        d2.add(10);

        rangeSignals.add(d2);

        //Dupla 3
        List<Integer> d3 = new ArrayList<>();
        d3.add(3);
        d3.add(12);

        rangeSignals.add(d3);

        //Dupla 4
        List<Integer> d4 = new ArrayList<>();
        d4.add(2);
        d4.add(6);

        rangeSignals.add(d4);

        //Dupla 5
        List<Integer> d5 = new ArrayList<>();
        d5.add(4);
        d5.add(9);

        rangeSignals.add(d5);

        //Lista de frecuencias
        List<Integer> frequencies = new ArrayList<>();
        frequencies.add(2);
        frequencies.add(3);
        frequencies.add(5);
        frequencies.add(8);
        frequencies.add(9);
        frequencies.add(10);
        frequencies.add(12);
        frequencies.add(16);

        System.out.println("Rango de señales de entrada:");

        rangeSignals.stream().forEach(m -> {
            //Imprimimos señales
            System.out.print(" [ ");
            m.stream().forEach(n -> System.out.print(n + " "));
            System.out.println("]");
        });

        System.out.println("Frecuencias: ");
        System.out.print(" [ ");
        frequencies.stream().forEach(n -> System.out.print(n + " "));
        System.out.println("]");

        //Aplicamos el filtro
        System.out.println("Calculamos el rango final de acuerdo a las frecuencias de entrada...");
        List<List<Integer>> rangeFreq = calculateRangeSuperposition(rangeSignals);
        if(!rangeFreq.isEmpty()) {
            filterFrequencies(rangeFreq, frequencies);
        } else {
            System.out.println("Ninguna frecuencia con que comparar.");
        }

    }

    public static void filterFrequencies(List<List<Integer>> rangeSignals, List<Integer> frequencies) {
        List<Integer> passFreq = new ArrayList<>();
        //Iteramos sobre la lista de frecuencias
        frequencies.stream().forEach( m -> {
            //Declaramos signalCount para saber si la frecuencia pasa
            AtomicInteger signalCount = new AtomicInteger(0);
            //Iteramos sobre cada elemento y utilizamos signalCount
            rangeSignals.stream().forEach(n -> {
                //Obtenemos el min y max de cada lista de señales
                AtomicInteger min = new AtomicInteger(Collections.min(n));
                AtomicInteger max = new AtomicInteger(Collections.max(n));
                System.out.println("Verificamos ["+min.get()+","+max.get()+"] con "+m);
                System.out.println("Condición "+m+" >= "+min.get()+" y "+m+" <= "+max.get());
                //Verificamos si la frecuencia se encuentra en el rango min y max
                if(m >= min.get() && m <= max.get()) {
                    System.out.println("Frecuencia pasa: "+m);
                    //Entra en rango de frecuencias, agregamos signalCount + 1
                    signalCount.addAndGet(1);
                }
            });
            //Si está en más de un rango de frecuencias, pasa
            if(signalCount.get() >= 1) {
                System.out.println("Agregamos a lista de frecuencias que pasan....");
                passFreq.add(m);
            }
            //Reset de signalCount
            signalCount.getAndSet(0);
        });

        //Verificamos si pasaron frecuencias
        if(passFreq.isEmpty()) {
            System.out.println("Ninguna frecuencia pasa.");
        } else {
            //Imprimimos la lista
            System.out.println("Frecuencias que pasan el filtro: ");
            System.out.print(" [ ");
            passFreq.stream().forEach(n -> System.out.print(n + " "));
            System.out.print("]");
        }

    }


    public static List<List<Integer>> calculateRangeSuperposition(List<List<Integer>> rangeSignals) {

        List<Integer> freq = new ArrayList<>();
        List<Integer> freqUnique = new ArrayList<>();
        List<List<Integer>> rangeSignal = new ArrayList<>();
        //Comparamos los arrays y obtenemos las duplas comparando cada lista de duplas con las siguientes
        rangeSignals.stream().forEach(dupla -> {
            System.out.println("----------------------------");
            //Obtenemos el min y max de cada lista de señales
            AtomicInteger min = new AtomicInteger(Collections.min(dupla));
            AtomicInteger max = new AtomicInteger(Collections.max(dupla));
            System.out.println("min -> "+min+", max -> "+max);
            //Validamos en cada dupla los rangos
            rangeSignals.stream().forEach(m -> {
                //Si no es la dupla actual comparamos
                if(!m.equals(dupla)) {
                    //Verificamos si la frecuencia se encuentra en el rango min y max
                    m.stream().forEach(n -> {
                        System.out.println("n -> " + n);
                        if(n >= min.get() && n <= max.get()) {
                            System.out.println("item en rango min - max -> " + n);
                            freq.add(n);
                            if(!freqUnique.contains(n)) {
                                freqUnique.add(n);
                            }
                        }
                    });
                }
            });
        });


        //Imprimimos la lista de elementos de las duplas nuevas
        System.out.println("Rango de señales obtenido:");

        System.out.print(" [ ");
        freqUnique.stream().forEach(n -> System.out.print(n + " "));
        System.out.println("]");

        System.out.println("Rango de señales en comun:");
        System.out.print(" [ ");
        freq.stream().forEach(n -> System.out.print(n + " "));
        System.out.println("]");

        System.out.println("Calculando rango.....");

        if(!freq.isEmpty()) {
            //Verificamos el rango más frecuente

                //Verificamos el número de veces que tiene cada item en la lista
                List<Integer> ocurr = new ArrayList<>();
                freqUnique.stream().forEach(m -> {
                    int ocurrences = Collections.frequency(freq, m);
                    System.out.println(" m -> " + m +" -> ocurrencias -> "+ ocurrences);
                    ocurr.add(ocurrences);
                });

                System.out.println("Ocurrencias entre señales: ");
                System.out.print(" [ ");
                //Verificamos los numeros mas grandes
                ocurr.stream().forEach(n -> System.out.print(n + " "));
                System.out.println("]");

                int maxSignal = Collections.max(ocurr);

                //Verificamos si hay dos numeros con ese max
                int ocurrMax = Collections.frequency(ocurr, maxSignal);

                System.out.println("Ocurr max value: "+ocurrMax);


                //Obtenemos min y max
                AtomicInteger min = new AtomicInteger(-1);
                AtomicInteger max = new AtomicInteger(-1);
                AtomicInteger index = new AtomicInteger(0);
                ocurr.stream().forEach(m -> {

                    //Obtenemos el minimo
                    if(ocurrMax == 1) {
                        if(m <= maxSignal && m >= min.get()) {
                            min.getAndSet(freqUnique.get(index.get()));
                        }
                    } else {
                        //Ya se encontro el maximo, falta otro número con la misma ocurrencia
                        if(max.get() != -1 && m == maxSignal) {
                            min.getAndSet(freqUnique.get(index.get()));
                        }
                    }

                    //Obtenemos el maximo
                    if(m == maxSignal && max.get() == -1) {
                        max.getAndSet(freqUnique.get(index.get()));
                    }

                    index.addAndGet(1);
                });

                List<Integer> result = new ArrayList<>();
                result.add(min.get());
                result.add(max.get());
                Collections.sort(result);

                System.out.println("Rango resultante: ");
                System.out.print(" [ ");
                //Verificamos los numeros mas grandes
                result.stream().forEach(n -> System.out.print(n + " "));
                System.out.println("]");

                rangeSignal.add(result);

        } else {
            System.out.println("No hay rango de frecuencias superpuestas.");
        }

        return rangeSignal;
    }
}
