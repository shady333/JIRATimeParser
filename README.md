# JIRATimeParser

If you whant to track logged work into JIRA you could:
1. Search for payed tool
2. Track logged work records manually
3. Write your own solution
4. Use proposed simple application

Let's stop at point 4. But, firstly, let me describe main issue with tracking logged work time in JIRA.

Workload Pie Chart could show you values of reported time by assignee. This work fine, when only assignee reports time to one ticket.
It will not show you diversity of logged times.

To get more redable logged time reports please follow next steps:
1. Download JIRATimeParser.jar from the repository
2. Go to your JIRA and export search results (you can search, for example all tickets with LoggedWork during current year) into CSV file format with all values and columns
3. Execute downloaded jar file: java -jar JIRATimeRarser.jar
4. Enter prepared JIRA export file location
5. Enter output file name (add .csv as an extension)
6. You can filter looged work time by month, just enter short month name, for example - Nov, to get all records in November. Or press enter to skip and
get report for whole records.
7. Press "c" key to start calculation.
8. Open created file and change format for time columnt into Category Time - Duration.

Please feel free to contact directly with me in case of any questions or issues.

Thanks,
Oleg
