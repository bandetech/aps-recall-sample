Alfresco Process Services Recall Sample
====

## Overview
Test code of recall process for Alfresco Process Services (APS) 

## Description

To recall process, add a receive task before task you want to draw back to and send signal. Then process recall and go to the receive task.

Sample assumes following workflow.

![sample workflow](https://github.com/bandetech/aps-recall-sample/images/sample-workflow.png)

AppTest sends signal at userTask2 and recall the process to receiveTask.

## Usage
Please refer AppTest.java and moveToTask() in the sample test code.
