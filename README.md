# Talkdesk Billing

Displays the cost of a set of call records stored in a file.

## How to run

To run the application use the following command in the application's folder:

```bash
$ bin/billing <path-to-file>
```

Replace `<path-to-file>` with the path to a file in the following [format](#file-format).

### File format

The file **must** have the following format:
```csv
time_of_start;time_of_finish;call_from;call_to
```

Where:
* `time_of_start`: When the call started in the format `HH:mm:ss`.
* `time_of_finish`: When the call ended in the format `HH:mm:ss`.
* `call_from`: Who started the call.
* `call_to`: To whom `call_from` is calling.

For example, consider the following `sample.csv` file:
```csv
09:11:30;09:15:22;+351914374373;+351215355312
15:20:04;15:23:49;+351217538222;+351214434422
16:43:02;16:50:20;+351217235554;+351329932233
17:44:04;17:49:30;+351914374373;+351963433432
```

**Note**:
* A call between `23:59:00` and `01:00:00` means that the call lasted 2 minutes.
* Both `time_of_start` and `time_of_finish` are in the same timezone.
* The following phone numbers are considered different: `+351911234567`, `00351911234567`, and `911234567`.

### Configuration

The simulation configuration is defined in the `conf/application.conf` file:
 
```
cost-format = "0.00"

csv {
  separator = ";"
  header = false
}
```
 
Where:
* `cost-format`: How the bill cost should be formatted. This uses the [DecimalFormat](https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html).
* `csv`: The configuration object of the `CSV` reader.
  * `separator`: If the CSV file contains an header.
  * `header`: If the CSV file contains an header.


## Billing 

### Cost per Call

| Duration of the call           | Cost Per Minute |
|--------------------------------|-----------------|
| First 5 minutes (0:00 to 4:59) | 0.05            |
| Following minutes              | 0.02            |


### Final Cost

The caller with the highest total call duration of the day will not be charged, even if he is the only caller in that day.

