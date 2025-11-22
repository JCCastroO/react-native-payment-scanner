#import "BarcodeScanner.h"
#import <AVFoundation/AVFoundation.h>

@implementation BarcodeScanner

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(requestCameraPermission:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    AVAuthorizationStatus status = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    
    if (status == AVAuthorizationStatusAuthorized) {
        resolve(@(YES));
    } else if (status == AVAuthorizationStatusNotDetermined) {
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
            resolve(@(granted));
        }];
    } else {
        resolve(@(NO));
    }
}

@end