package com.log.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Logreader {
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	static Date logStarttime = null;
	static Date logEndtime = null;
	static List<String> listWithoutDuplicates = null;
	static List<String> nameList = new ArrayList<>();

	public static void main(String[] args) {

		if (args.length == 1) {

			try {

				List<Result> resultList = new ArrayList<Result>();
				List<List<Details>> finallistoflist = new ArrayList<List<Details>>();
				List<List<Details>> storageList = new ArrayList<List<Details>>();
				List<Details> misMatching = new ArrayList<Details>();
				Map<String, Integer> removed= new HashMap<String, Integer>();
				FileReader fr = new FileReader(args[0]);
				BufferedReader br = new BufferedReader(fr);
				StringBuffer logtxt = new StringBuffer();
				Integer startCounter = 1;
				Integer endCounter = 1;

				int i;
				while ((i = br.read()) != -1) {
					logtxt.append((char) i);
				}
				br.close();
				fr.close();
				String finalTxt = logtxt.toString();
				storageList = makeList(finalTxt);
				finallistoflist=storageList ;
				

				for (int j = 0; j < listWithoutDuplicates.size(); j++) {
					String name = listWithoutDuplicates.get(j);
					// System.err.println(name);
					for (int k = 0; k < finallistoflist.size(); k++) {
						if (finallistoflist.get(k).get(0).getName().equalsIgnoreCase(name)) {
							// System.err.println(finallistoflist.get(k).get(0).getStatus());
							if (finallistoflist.get(k).get(0).getStatus().equalsIgnoreCase("END")) {
								removed.put(finallistoflist.get(k).get(0).getName(), 1);
								misMatching.add(finallistoflist.get(k).get(0));
								finallistoflist.get(k).remove(0);
								
								
							}

						}
					}
				}

				/*
				 * for (Details list : misMatching) { System.out.println("Mismatch "+list); }
				 */

				for (int j = 0; j < listWithoutDuplicates.size(); j++) {
					String name = listWithoutDuplicates.get(j);
					startCounter = 1;
					endCounter = 1;
					for (int k = 0; k < finallistoflist.get(j).size(); k++) {
						if (finallistoflist.get(j).get(k).getName().equals(name)) {
							if (finallistoflist.get(j).get(k).getStatus().equalsIgnoreCase("Start")) {

								finallistoflist.get(j).get(k).setLoc(startCounter);
								startCounter++;

							} else if (finallistoflist.get(j).get(k).getStatus().equalsIgnoreCase("End")) {
								finallistoflist.get(j).get(k).setLoc(endCounter);
								endCounter++;
							}
						}

					}

				}

				for (int j = 0; j < listWithoutDuplicates.size(); j++) {
					String name = listWithoutDuplicates.get(j);
					startCounter = 1;
					endCounter = 1;
					long totalTime = 0;
					// System.out.println("Name "+name);
					for (int k = 0; k < finallistoflist.get(j).size(); k++) {
						Map<String, Long> counted = finallistoflist.get(j).stream()
								.collect(Collectors.groupingBy(Details::getStatus, Collectors.counting()));
						if (finallistoflist.get(j).get(k).getName().equals(name)) {
							if (finallistoflist.get(j).get(k).getStatus().equalsIgnoreCase("Start")
									&& k != finallistoflist.get(j).size() - 1) {
								for (int k2 = k; k2 < finallistoflist.get(j).size(); k2++) {
									if (finallistoflist.get(j).get(k2).getStatus().equalsIgnoreCase("End")) {
										if (finallistoflist.get(j).get(k).getLoc() == finallistoflist.get(j).get(k2)
												.getLoc()) {
											if (finallistoflist.get(j).get(k).getTime()
													.before(finallistoflist.get(j).get(k2).getTime())) {
												// System.out.println("matched");
												long difference_In_Seconds = (finallistoflist.get(j).get(k2).getTime()
														.getTime() - finallistoflist.get(j).get(k).getTime().getTime());
												long seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Seconds);
												totalTime = totalTime + seconds;
												// System.out.println("Total "+totalTime+"difference_In_Seconds
												// "+seconds +" Start ,"+finallistoflist.get(j).get(k).getTime()+" End
												// ,"+finallistoflist.get(j).get(k2).getTime());
												break;

											}
											/*
											 * else { misMatching.add(finallistoflist.get(j).get(k2)); }
											 */

										}
									}

								}

							} else if (finallistoflist.get(j).get(k).getLoc() == 0
									|| finallistoflist.get(j).get(k).getLoc() > counted.get("Start")) {
								misMatching.add(finallistoflist.get(j).get(k));
							} else if (finallistoflist.get(j).get(k).getStatus().equalsIgnoreCase("Start")
									&& k == finallistoflist.get(j).size() - 1) {
								misMatching.add(finallistoflist.get(j).get(k));
							}
						}

					}
					Result rs = new Result();
					rs.setName(name);
					rs.setDuration(totalTime);
					resultList.add(rs);

				}

				for (int j = 0; j < resultList.size(); j++) {
					for (int j2 = 0; j2 < misMatching.size(); j2++) {
						if (resultList.get(j).getName().equals(misMatching.get(j2).getName())) {
							// System.err.println(resultList.get(j).getName()+"
							// "+misMatching.get(j2).getName());
							if (misMatching.get(j2).getStatus().equalsIgnoreCase("start")) {
								long dif = ( logEndtime.getTime()- misMatching.get(j2).getTime().getTime() );
								long seconds = TimeUnit.MILLISECONDS.toSeconds(dif);
								resultList.get(j).setDuration(resultList.get(j).getDuration()+seconds);
								// System.out.println(resultList.get(j));
							} else {
								long dif = (misMatching.get(j2).getTime().getTime() - logStarttime.getTime());
								long seconds = TimeUnit.MILLISECONDS.toSeconds(dif);
								resultList.get(j).setDuration(resultList.get(j).getDuration() + seconds);
							}
							long countStratSession = storageList.get(j)
									  .stream()
									  .filter(c -> c.getStatus().equalsIgnoreCase("Start") )
									  .count();
							long countEndSession = storageList.get(j)
									  .stream()
									  .filter(c -> c.getStatus().equalsIgnoreCase("End") )
									  .count();
							//System.err.println("countEndSession "+countEndSession+" , countStratSession "+countStratSession);
							if(removed.containsKey(resultList.get(j).getName())) {
							if(countStratSession>countEndSession)
								resultList.get(j).setNumSession((int) countStratSession+removed.get(resultList.get(j).getName()));
							else
								resultList.get(j).setNumSession((int) countEndSession+removed.get(resultList.get(j).getName()));
							}else {
								if(countStratSession>countEndSession)
									resultList.get(j).setNumSession((int) countStratSession);
								else
									resultList.get(j).setNumSession((int) countEndSession);
							}
						}

					}
				}

				for (Result details : resultList) {
					System.out.println(details);
				}

			} catch (Exception e) {
				System.out.println("Caught EXception " + e);
			}
		} else
			System.out.println("Invalid Arguments Passed");
	}

	public static List<List<Details>> makeList(String finalTxt) {
		List<Details> detailList = new ArrayList<Details>();
		List<Details> sortedList = new ArrayList<Details>();
		List<List<Details>> result = new ArrayList<List<Details>>();

		String[] str = finalTxt.split("\\n");
		List<String> strlist = Arrays.asList(str);

		for (String string : strlist) {
			String[] details = string.split("\\s");
			if (details.length == 3) {
				Details detail = new Details();
				try {
					detail.setTime(sdf.parse(details[0]));
				} catch (ParseException e) {
					//System.out.println("Unparsable date moving to next element");
					continue;
				}
				detail.setName(details[1]);
				if (details[2].equalsIgnoreCase("start") || details[2].equalsIgnoreCase("end")) {
					detail.setStatus(details[2]);
				} else {
					//System.out.println("Invalid Element moving to next element");
					continue;
				}
				detailList.add(detail);
			} else {
				//System.out.println("Invalid Element moving to next element");
				continue;
			}
		}
		logStarttime = detailList.get(0).getTime();
		logEndtime = detailList.get(detailList.size() - 1).getTime();
		// System.out.println("logStarttime "+logStarttime);
		// System.out.println("logEndtime "+logEndtime);
		sortedList = detailList.stream().sorted(Comparator.comparing(Details::getName)).collect(Collectors.toList());
		for (Details detailed : sortedList) {
			nameList.add(detailed.getName());
			// System.out.println(detailed.getTime() + " " + detailed.getName() + " " +
			// detailed.getStatus());
		}
		listWithoutDuplicates = new ArrayList<>(new HashSet<>(nameList));
		List<Details> seperatedName = new ArrayList<Details>();
		for (int j = 0; j < listWithoutDuplicates.size(); j++) {
			String name = listWithoutDuplicates.get(j);
			// System.err.println(name);
			int statusStartCounter=0;
			int statusEndCounter =0;
			seperatedName = new ArrayList<Details>();
			for (int k = 0; k < sortedList.size(); k++) {

				if (sortedList.get(k).getName().equals(name)) {
					seperatedName.add(sortedList.get(k));
				}
			}
			
			result.add(seperatedName);

		}
		/*
		 * for (List<Details> details : result) {
		 * 
		 * for (Details shoe : details) { System.err.println(shoe);
		 * 
		 * } }
		 */

		return result;

	}

}
