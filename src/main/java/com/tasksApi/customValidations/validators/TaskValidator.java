package com.tasksApi.customValidations.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tasksApi.model.Tasks;
import com.tasksApi.requests.TaskAssignRequest;
import com.tasksApi.requests.TaskCommentRequest;

import org.springframework.stereotype.Component;

import com.tasksApi.enums.TaskStatusEnum;
import com.tasksApi.enums.TaskTypeEnum;

@Component
public class TaskValidator {

	ArrayList<String> validationMessages = new ArrayList<>();

    public ArrayList<String> validate(Tasks task)
    {
        if (task.getTitle() == null) {
            validationMessages.add("MISSING_TITLE");
        } else {
            String taskTitle = task.getTitle().trim();

            if (taskTitle.equals("")) {
                validationMessages.add("EMPTY_TITLE");
            }

            try {
                Integer.parseInt(taskTitle);
                validationMessages.add("TITLE_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("Title is not integer");
            }
        }

        if (task.getDescription() == null) {
            validationMessages.add("MISSING_DESCRIPTION");
        } else {
            String taskDescription = task.getDescription().trim();

            if (taskDescription.equals("")) {
                validationMessages.add("EMPTY_DESCRIPTION");
            }

            try {
                Integer.parseInt(taskDescription);
                validationMessages.add("DESCRIPTION_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("E-Mail is not integer");
            }
        }

        if (task.getType() == null) {
            validationMessages.add("MISSING_TYPE");
        } else {
            String taskType = task.getType().trim();

            if (taskType.equals("")) {
                validationMessages.add("EMPTY_TYPE");
            }

            try {
                Integer.parseInt(taskType);
                validationMessages.add("TYPE_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("Type is not integer");
            }

            List<String> availableTypes = Stream.of(TaskTypeEnum.class.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());

            if (!availableTypes.contains(taskType)) {
                validationMessages.add("INVALID_TYPE");
            }
        }

        if (task.getStatus() != null) {
            String taskStatus = task.getStatus().trim();

            if (taskStatus.equals("")) {
                validationMessages.add("EMPTY_STATUS");
            }

            try {
                Integer.parseInt(taskStatus);
                validationMessages.add("STATUS_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("Status is not integer");
            }

            List<String> availableStatus = Stream.of(TaskStatusEnum.class.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());

            if (!availableStatus.contains(taskStatus)) {
                validationMessages.add("INVALID_STATUS");
            }
        }

        return validationMessages;
    }

    public ArrayList<String> validateOnListingOrUpdate(Tasks task)
    {
        if (task.getTitle() != null) {
            String taskTitle = task.getTitle().trim();

            if (taskTitle.equals("")) {
                validationMessages.add("EMPTY_TITLE");
            }

            try {
                Integer.parseInt(taskTitle);
                validationMessages.add("TITLE_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("Title is not integer");
            }
        }

        if (task.getDescription() != null) {
            String taskDescription = task.getDescription().trim();

            if (taskDescription.equals("")) {
                validationMessages.add("EMPTY_DESCRIPTION");
            }

            try {
                Integer.parseInt(taskDescription);
                validationMessages.add("DESCRIPTION_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("E-Mail is not integer");
            }
        }

        if (task.getType() != null) {
            String taskType = task.getType().trim();

            if (taskType.equals("")) {
                validationMessages.add("EMPTY_TYPE");
            }

            boolean isString = false;
            try {
                Integer.parseInt(taskType);
                validationMessages.add("TYPE_NOT_STRING");
            } catch (NumberFormatException nfe) {
                isString = true;
                System.out.println("Type is not integer");
            }

            if (isString) {
                List<String> availableTypes = Stream.of(TaskTypeEnum.class.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.toList());

                if (!availableTypes.contains(taskType)) {
                    validationMessages.add("INVALID_TYPE");
                }
            }
        }

        if (task.getStatus() != null) {
            String taskStatus = task.getStatus().trim();

            if (taskStatus.equals("")) {
                validationMessages.add("EMPTY_STATUS");
            }

            boolean isString = false;
            try {
                Integer.parseInt(taskStatus);
                validationMessages.add("STATUS_NOT_STRING");
            } catch (NumberFormatException nfe) {
                isString = true;
                System.out.println("Status is not integer");
            }

            if (isString) {
                List<String> availableStatus = Stream.of(TaskStatusEnum.class.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.toList());

                if (!availableStatus.contains(taskStatus)) {
                    validationMessages.add("INVALID_STATUS");
                }
            }
        }

        return validationMessages;
    }

    public ArrayList<String> validateAssignment(TaskAssignRequest taskAssign)
    {
        if (taskAssign.getAssignedTo() == null) {
            validationMessages.add("MISSING_ASSIGNED_TO");
        } else {
            try {
                Integer.parseInt(taskAssign.getAssignedTo());
            } catch (NumberFormatException nfe) {
                validationMessages.add("ASSIGNED_TO_NOT_INTEGER");
            }
        }

        return validationMessages;
    }

    public ArrayList<String> validateComment(TaskCommentRequest taskComment)
    {
        if (taskComment.getText() == null) {
            validationMessages.add("MISSING_TEXT");
        } else {
            String comment = taskComment.getText().trim();

            if (comment.equals("")) {
                validationMessages.add("EMPTY_TEXT");
            }

            try {
                Integer.parseInt(comment);
                validationMessages.add("TEXT_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("Title is not integer");
            }
        }

        return validationMessages;
    }
}
