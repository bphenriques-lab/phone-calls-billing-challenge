Challenge made in 2017.

Some thoughts in 2020:
- Good that I went light on dependencies.
- Liked the way I approved acceptance tests.
- Too many docs. Unnecessary as the code is self-explanatory (as it should be).
- Good abstractions as they are composable but not necessary in the scope of this challenge.
- Licence?
---

# Goal

Given a list of calls with the following format:

    time_of_start;time_of_finish;call_from;call_to

And the following rules:

 - The first 5 minutes of each call are billed at 5 cents per minute
 - The remainer of the call is billed at 2 cents per minute
 - The caller with the highest total call duration of the day will not be charged (i.e., the caller that has the highest total call duration among all of its calls)

Calculate the total cost for these calls.

## Additional Notes

* A call between `23:59:00` and `01:00:00` means that the call lasted 2 minutes.
* Both `time_of_start` and `time_of_finish` are in the same timezone.
* The following phone numbers are considered different: `+351911234567`, `00351911234567`, and `911234567`.

# Example

Input:
```
09:11:30;09:15:22;+351914374373;+351215355312
15:20:04;15:23:49;+351217538222;+351214434422
16:43:02;16:50:20;+351217235554;+351329932233
17:44:04;17:49:30;+351914374373;+351963433432
```

Output:
``` 
15.05
``` 

## Running


```bash
$ bin/billing <path-to-file>
```

Where `<path-to-file>` is the path to the file.

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

