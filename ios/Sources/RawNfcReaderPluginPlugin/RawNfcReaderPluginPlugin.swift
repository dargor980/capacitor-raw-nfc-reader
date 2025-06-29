import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(RawNfcReaderPluginPlugin)
public class RawNfcReaderPluginPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "RawNfcReaderPluginPlugin"
    public let jsName = "RawNfcReaderPlugin"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = RawNfcReaderPlugin()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
