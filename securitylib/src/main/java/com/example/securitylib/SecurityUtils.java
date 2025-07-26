package com.example.securitylib;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

public class SecurityUtils {

    public static void applySecurity(Activity activity, SecurityConfig config) {
        if (config.isBlockScreenshotsAndRecentApps()) {
            applyWindowSecurityFlags(activity);
        } else {
            clearWindowSecurityFlags(activity);
        }

        if (config.isBlockCopyPaste()) {
            disableCopyPasteForAllEditTexts(activity);
            clearClipboard(activity);
        }
    }

    private static void applyWindowSecurityFlags(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        Log.d("SecurityUtils", "FLAG_SECURE enabled - Screenshots and recent apps blocked");
    }

    private static void clearWindowSecurityFlags(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        Log.d("SecurityUtils", "FLAG_SECURE disabled - Screenshots and Recent Apps allowed");
    }

    public static void disableCopyPasteForAllEditTexts(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        disableCopyPasteRecursive(rootView);
    }

    private static void disableCopyPasteRecursive(View view) {
        if (view instanceof EditText) {
            disableCopyPaste((EditText) view);
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                disableCopyPasteRecursive(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    public static void disableCopyPaste(EditText editText) {
        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) { return false; }
            @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
            @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) { return false; }
            @Override public void onDestroyActionMode(ActionMode mode) { }
        });

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearClipboard(editText.getContext());
            }

            @Override public void afterTextChanged(Editable s) { }
        });
    }

    public static void clearClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                clipboard.clearPrimaryClip();
            } else {
                clipboard.setPrimaryClip(ClipData.newPlainText("", ""));
            }
            Log.d("SecurityUtils", "Clipboard emptied");
        }
    }
}