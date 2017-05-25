package com.logn.yunupan.activity.scan;

/**
 * Created by liufengkai on 16/9/25.
 */

public class ScanQRPresenter implements ScanQRContract.Presenter {

    private ScanQRContract.View mView;

    public ScanQRPresenter(ScanQRContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getSubscribeInfo(String id) {
//        Observable<OfficialInfo> observable = APIManager.getInstance().getOfficialInfo(id);
//        observable.compose(SchedulersCompat.<OfficialInfo>applyIoSchedulers())
//                .subscribe(new HandlerSubscriber<OfficialInfo>(mView.getContext()) {
//                    @Override
//                    public void success(OfficialInfo officialInfo) {
//                        mView.intent2SubscribePage(officialInfo);
//                    }
//
//                    @Override
//                    public void error(Throwable e) {
//                        e.printStackTrace();
//                        mView.onFailed();
//                    }
//
//                    @Override
//                    public void completed() {
//
//                    }
//                });

    }

    @Override
    public void getSamplePersonInfo(String id) {
//        int number;
//        try {
//            number = Integer.parseInt(id);
//        } catch (NumberFormatException e) {
//            mView.onFailed();
//            return;
//        }
//
//        Observable<SocialInfo> observable = APIManager.getInstance().getSocialInfo(number);
//        observable.compose(SchedulersCompat.<SocialInfo>applyIoSchedulers())
//                .subscribe(new HandlerSubscriber<SocialInfo>(mView.getContext()) {
//                    @Override
//                    public void success(SocialInfo socialInfo) {
//                        PersonInfo.PersonInfoBean bean = new PersonInfo.PersonInfoBean();
//                        bean.setId(socialInfo.getSocialInfo().getId());
//                        bean.setHead_img(socialInfo.getSocialInfo().getHeadImage());
//                        bean.setReal_name(socialInfo.getSocialInfo().getNickName());
//                        bean.setSex(socialInfo.getSocialInfo().getSex());
//                        mView.intent2PersonInfoSample(bean);
//                    }
//
//                    @Override
//                    public void error(Throwable e) {
//                        e.printStackTrace();
//                        mView.onFailed();
//                    }
//
//                    @Override
//                    public void completed() {
//
//                    }
//                });
    }

    @Override
    public void getClassInfo(String id, String courseNum) {
//        Observable<Courses> observable = APIManager.getInstance().getCourse(id, courseNum);
//        observable.compose(SchedulersCompat.<Courses>applyIoSchedulers())
//                .subscribe(new HandlerSubscriber<Courses>(mView.getContext()) {
//                    @Override
//                    public void success(Courses course) {
//                        if (course != null && course.getCourses().size() >= 1) {
//                            mView.intent2ClassAddPage(course.getCourses().get(0));
//                        }
//                    }
//
//                    @Override
//                    public void error(Throwable e) {
//                        e.printStackTrace();
//                        mView.onFailed();
//                    }
//
//                    @Override
//                    public void completed() {
//
//                    }
//                });
    }
}
