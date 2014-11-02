package com.schwergsy.kentstate;

import com.parse.ParseObject;

public class JobMetaData implements Comparable<JobMetaData> {

    ParseObject jobInfo;
    double distanceToJob;
    int remainingTime;
    double runnerRating;
    double payment;
    SortType sortType;

    public JobMetaData(ParseObject jobInfo, double distanceToJob, int remainingTime,
                       double runnerRating, double payment, SortType sortType) {
        this.jobInfo = jobInfo;
        this.distanceToJob = distanceToJob;
        this.remainingTime = remainingTime;
        this.runnerRating = runnerRating;
        this.payment = payment;
        this.sortType = sortType;
    }

    public ParseObject getJobInfo() {
        return jobInfo;
    }

    public double getDistanceToJob() {
        return distanceToJob;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public double getRunnerRating() {
        return runnerRating;
    }

    public double getPayment() {
        return payment;
    }

    @Override
    //this is how MetaDatas are compared, returning -1 indicates this metaData is 'better' than
    //the parameter metaData, 0 is same, 1 is 'worse'
    public int compareTo(JobMetaData jobMetaData) {
        if (sortType.equals("proximity")) {

            double d1 = this.getDistanceToJob();
            double d2 = jobMetaData.getDistanceToJob();

            if (d1 < d2)
                return -1;
            else if (d1 > d2)
                return 1;
            else
                return 0;
        } else if (sortType.equals("rating")) {

            double r1 = this.getRunnerRating();
            double r2 = jobMetaData.getRunnerRating();

            if (r1 < r2)
                return 1;
            else if (r1 > r2)
                return -1;
            else
                return 0;
        } else if (sortType.equals("payment")) {

            double p1 = this.getPayment();
            double p2 = jobMetaData.getPayment();

            if (p1 < p2)
                return 1;
            else if (p1 > p2)
                return -1;
            else
                return 0;
        } else if (sortType.equals("time")) {

            double t1 = this.getRemainingTime();
            double t2 = jobMetaData.getRemainingTime();

            if (t1 < t2)
                return 1;
            else if (t1 > t2)
                return -1;
            else
                return 0;
        } else
            return 69;
    }
}
