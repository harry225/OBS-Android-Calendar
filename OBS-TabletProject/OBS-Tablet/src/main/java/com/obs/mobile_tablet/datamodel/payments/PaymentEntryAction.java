package com.obs.mobile_tablet.datamodel.payments;

/**
 * Created by stevenguitar on 1/7/14.
 */
class PaymentEntryAction {
    private boolean canEdit;
    private boolean canApprove;
    private boolean canReject;
    private boolean canCancel;
    private boolean canRelease;
    private boolean requireSecondaryAuthForApproval;
    private boolean requireSecondaryAuthForReject;

    boolean getRequireSecondaryAuthForReject() {
        return requireSecondaryAuthForReject;
    }

    void setRequireSecondaryAuthForReject(boolean requireSecondaryAuthForReject) {
        this.requireSecondaryAuthForReject = requireSecondaryAuthForReject;
    }

    boolean getRequireSecondaryAuthForApproval() {
        return requireSecondaryAuthForApproval;
    }

    void setRequireSecondaryAuthForApproval(boolean requireSecondaryAuthForApproval) {
        this.requireSecondaryAuthForApproval = requireSecondaryAuthForApproval;
    }

    boolean getCanApprove() {
        return canApprove;
    }

    void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    boolean getCanCancel() {
        return canCancel;
    }

    void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    boolean getCanEdit() {
        return canEdit;
    }

    void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    boolean getCanReject() {
        return canReject;
    }

    void setCanReject(boolean canReject) {
        this.canReject = canReject;
    }

    boolean getCanRelease() {
        return canRelease;
    }

    void setCanRelease(boolean canRelease) {
        this.canRelease = canRelease;
    }
}