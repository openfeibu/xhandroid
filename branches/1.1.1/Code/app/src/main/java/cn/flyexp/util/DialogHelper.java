package cn.flyexp.util;

import android.content.Context;

import cn.flyexp.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/26.
 */
public class DialogHelper {

    public static SweetAlertDialog getProgressDialog(Context context, String title) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.light_blue));
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setCancelable(false);
        return sweetAlertDialog;
    }

    public static void showSingleDialog(Context context, String msg, boolean isCancel, SweetAlertDialog.OnSweetClickListener clickListener) {
        showSingleDialog(context, msg, isCancel, context.getString(R.string.dialog_comfirm), clickListener);
    }

    public static void showSingleDialog(Context context, String msg, SweetAlertDialog.OnSweetClickListener clickListener) {
        showSingleDialog(context, msg, true, context.getString(R.string.dialog_comfirm), clickListener);
    }

    public static void showSingleDialog(Context context, String msg, boolean isCancel, String comfirmMsg, SweetAlertDialog.OnSweetClickListener clickListener) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText(msg);
        sweetAlertDialog.setConfirmText(comfirmMsg);
        sweetAlertDialog.setCanceledOnTouchOutside(isCancel);
        sweetAlertDialog.setConfirmClickListener(clickListener);
        sweetAlertDialog.show();
    }

    public static void showSelectDialog(Context context, String msg, SweetAlertDialog.OnSweetClickListener clickListener) {
        showSelectDialog(context, msg, context.getString(R.string.dialog_comfirm), context.getResources().getString(R.string.dialog_cancel), clickListener);
    }

    public static void showSelectDialog(Context context, String msg, String comfirmMsg, SweetAlertDialog.OnSweetClickListener clickListener) {
        showSelectDialog(context, msg, comfirmMsg, context.getResources().getString(R.string.dialog_cancel), clickListener);
    }

    public static void showSelectDialog(Context context, String msg, String comfirmMsg, String cancelMsg, SweetAlertDialog.OnSweetClickListener clickListener) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText(msg);
        sweetAlertDialog.setCancelable(true);
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.setConfirmClickListener(clickListener);
        sweetAlertDialog.showCancelButton(true);
        sweetAlertDialog.setConfirmText(comfirmMsg);
        sweetAlertDialog.setCancelText(cancelMsg);
        sweetAlertDialog.show();
    }

    public static void showErrorDialog(Context context,String error){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(error);
        sweetAlertDialog.setCancelable(true);
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.setConfirmText(context.getResources().getString(R.string.dialog_comfirm));
        sweetAlertDialog.show();
    }

    public static void showSuccessDialog(Context context,String msg){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(msg);
        sweetAlertDialog.setCancelable(true);
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.setConfirmText(context.getResources().getString(R.string.dialog_comfirm));
        sweetAlertDialog.show();
    }
}
