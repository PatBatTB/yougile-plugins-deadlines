package io.github.patbattb.yougile.plugins.deadlines;

import io.github.patbattb.plugins.core.expection.PluginCriticalException;
import io.github.patbattb.plugins.core.expection.PluginInterruptedException;
import io.github.patbattb.yougileapilib.domain.Deadline;
import io.github.patbattb.yougileapilib.domain.Task;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TaskHandler implements AutoCloseable {

    private final Parameters parameters;
    private final RequestSender requestSender;

    TaskHandler(Parameters parameters) {
        this.parameters = parameters;
        this.requestSender = new RequestSender(parameters.getToken(), parameters.getRequestCountPerMinute());
    }

    void processDailyColumn() throws PluginCriticalException, PluginInterruptedException {
        List<Task> taskList = requestSender.getTasks(parameters.getDailyColumId());
        List<Task> updatedTaskList = addDeadlines(taskList, TaskType.DAILY);
        requestSender.updateTasks(updatedTaskList);
    }

    void processWeeklyColumn() throws PluginCriticalException, PluginInterruptedException {
        List<Task> taskList = requestSender.getTasks(parameters.getWeeklyColumnId());
        List<Task> updatedTaskList = addDeadlines(taskList, TaskType.WEEKLY);
        requestSender.updateTasks(updatedTaskList);
    }

    private List<Task> addDeadlines(List<Task> taskList, TaskType taskType) {
        long deadlineTime = switch (taskType) {
            case DAILY -> getDailyDeadline();
            case WEEKLY -> getWeeklyDeadline();
            default -> throw new RuntimeException("unknown taskType");
        };
        List<Task> updatedTaskList = new ArrayList<>();
        for (Task task: taskList) {
            if (task.getDeadline() == null || task.getDeadline().getDeadline() == null ||
            task.getDeadline().getDeadline() > deadlineTime) {
                Deadline deadline = new Deadline(deadlineTime, null, false);
                task.setDeadline(deadline);
                updatedTaskList.add(task);
            }
        }
        return updatedTaskList;
    }

    private long getDailyDeadline() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        zonedDateTime = zonedDateTime.truncatedTo(ChronoUnit.DAYS);
        return zonedDateTime.toInstant().toEpochMilli();
    }

    private long getWeeklyDeadline() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        long daysToEndOfWeek = 7 - zonedDateTime.getDayOfWeek().getValue();
        zonedDateTime = zonedDateTime.truncatedTo(ChronoUnit.DAYS)
                .plusDays(daysToEndOfWeek);
        return zonedDateTime.toInstant().toEpochMilli();
    }

    void shutdown() {
        requestSender.shutdown();
    }

    @Override
    public void close() {
        requestSender.close();
    }
}
