package mixin;

//@Mixin(Minecraft.class)
//public class ExampleMixin {
//    @Shadow
//    @Final
//    private User user;
//
//    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", locals = LocalCapture.CAPTURE_FAILHARD)
//    private void init(GameConfig gameConfig, CallbackInfo callbackInfo, File file1) {
//        System.out.println("Mixin : Hello Forge Template!");
//        System.out.println("Mixin : Hello " + this.user.getName() + "!");
//        System.out.println("Mixin : Asset Dir - " + file1.getAbsolutePath());
//    }
//}