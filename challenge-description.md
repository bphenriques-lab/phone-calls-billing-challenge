# cdbb v0.1

This exercise is part of Talkdesk's recruitment process. Please do not share it publicly.

It is up to you how much effort you put into it and exactly what you deliver; we would expect you to spend one or two hours on it, and that you deliver at least a piece of working code that we'll be able to run on our machines* and that will solve the problem at hand.

It is also up to you whether there's any need for documentation (or anything else), depending on the problem you've been assigned or the method you chose to solve it.

## Exercise

Given a list of calls with the following format:

    time_of_start;time_of_finish;call_from;call_to

And the following rules:

 - The first 5 minutes of each call are billed at 5 cents per minute
 - The remainer of the call is billed at 2 cents per minute
 - The caller with the highest total call duration of the day will not be charged (i.e., the caller that has the highest total call duration among all of its calls)

Calculate the total cost for these calls.

Here's a sample file:

09:11:30;09:15:22;+351914374373;+351215355312
15:20:04;15:23:49;+351217538222;+351214434422
16:43:02;16:50:20;+351217235554;+351329932233
17:44:04;17:49:30;+351914374373;+351963433432

## Interface

The interface we're expecting is:

  $ your_script input_file

As for output, we're expecting that your code print the total in decimal format, with no currency symbols; e.g.:

  15.05

## Other notes

* - "our machines" usually run Ruby, Java, C#, C, C++, PHP, Perl, Python, Node, Elixir, Clojure, Kotlin, Go and Scala. If you want to go with a different programming language it shouldn't be a problem, but please do check with us first. We strongly encourage you to use a language you're familiar with.
