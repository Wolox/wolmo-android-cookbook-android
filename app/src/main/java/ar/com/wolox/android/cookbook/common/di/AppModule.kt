package ar.com.wolox.android.cookbook.common.di

import ar.com.wolox.android.cookbook.recipepicker.RecipePickerActivity
import ar.com.wolox.android.cookbook.recipepicker.RecipePickerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @ContributesAndroidInjector
    abstract fun recipePickerActivity(): RecipePickerActivity

    @ContributesAndroidInjector
    abstract fun recipePickerFragment(): RecipePickerFragment
}
