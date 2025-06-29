import { RawNfcReaderPlugin } from 'raw-nfc-reader';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    RawNfcReaderPlugin.echo({ value: inputValue })
}
