import Foundation

@objc public class RawNfcReaderPlugin: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
