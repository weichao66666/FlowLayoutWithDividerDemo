package io.weichao.flowlayoutwithdividerdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.weichao.flowlayoutwithdividerdemo.util.ViewUtil;
import io.weichao.flowlayoutwithdividerdemo.widget.FlowLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowLayout flowLayout = findViewById(R.id.flow_layout);

        List<String> flowLayoutData = new ArrayList<>();
        flowLayoutData.add("计算机");
        flowLayoutData.add("回收站");
        flowLayoutData.add("美图秀秀");
        flowLayoutData.add("美图秀秀批处理");
        flowLayoutData.add("MarkdownPad 2");
        flowLayoutData.add("Steam");
        flowLayoutData.add("华为手机助手");
        flowLayoutData.add("雷神加速器");
        flowLayoutData.add("R X64 3.4.2");
        flowLayoutData.add("Google Chrome");
        flowLayoutData.add("简历");
        flowLayoutData.add("data");
        flowLayoutData.add("变系数部分非线性模型");
        flowLayoutData.add("eclipse");
        flowLayoutData.add("studio2");
        flowLayoutData.add("android_studio_workspace - 快捷方式");
        flowLayoutData.add("eclipse_workspace - 快捷方式");
        flowLayoutData.add("Internet Explorer");
        flowLayoutData.add("Photoshop");
        flowLayoutData.add("XMind");
        flowLayoutData.add("计算器");
        flowLayoutData.add("画图");
        flowLayoutData.add("命令提示符");
        flowLayoutData.add("Visual Studio 2015");
        flowLayoutData.add("visual_studio_workspace - 快捷方式");
        flowLayoutData.add("web_workspace - 快捷方式");
        flowLayoutData.add("git_workspace - 快捷方式");
        flowLayoutData.add("ATOM 图片分割");
        flowLayoutData.add("ScreenToGif");
        flowLayoutData.add("格式工厂");
        flowLayoutData.add("Samsung Kies");
        flowLayoutData.add("apktool");
        flowLayoutData.add("小汽车指标.txt");
        flowLayoutData.add("Axure RP");
        flowLayoutData.add("color.exe - 快捷方式");
        flowLayoutData.add("bsszjc_Android - 快捷方式");
        flowLayoutData.add("studio3");
        flowLayoutData.add("PLAYERUNKNOWN'S BATTLEGROUNDS");
        flowLayoutData.add("RStudio");

        flowLayout.setAdapter(new MyFlowLayoutAdapter(this, flowLayoutData));
    }

    private class MyFlowLayoutAdapter extends FlowLayout.FlowLayoutAdapter {
        private Context context;
        private List<String> flowLayoutData;

        private MyFlowLayoutAdapter(Context context, List<String> flowLayoutData) {
            super(context);
            this.context = context;
            this.flowLayoutData = flowLayoutData;
        }

        @Override
        public List<String> getData() {
            return flowLayoutData;
        }

        @Override
        public View getDividerView() {
            return View.inflate(context, R.layout.divider, null);
        }

        @Override
        public int getDividerViewWidth() {
            return ViewUtil.dp2px(context, 1);
        }

        @Override
        public int getDividerViewHeight() {
            return ViewUtil.dp2px(context, 15);
        }

        @Override
        public int getGravityMode() {
//            return FlowLayout.FlowLayoutAdapter.GRAVITY_MODE_LEFT;
            return FlowLayout.FlowLayoutAdapter.GRAVITY_MODE_LEFT_AND_RIGHT;
        }

        @Override
        public int getDividerViewMinMargin() {
            return ViewUtil.dp2px(context, 10);
        }
    }
}
